package com.megateam.server.database;

import com.megateam.common.data.Ticket;
import com.megateam.common.data.util.LocalDateTimeAdapter;
import com.megateam.common.data.validation.TicketValidator;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.FileException;
import com.megateam.common.exception.ValidationException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;
import com.megateam.common.exception.impl.file.FileNotExistsException;
import com.megateam.common.util.FileManipulationService;
import com.megateam.server.database.data.TicketDatabaseDataclass;

import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/** Class for saving database into a file */
@RequiredArgsConstructor
public class DatabaseSavingService {
    /** FileManipulationService instance */
    private final FileManipulationService fms;

    /** Database saving file */
    private final File savingFile;

    /** JAXB marshaller instance */
    private final Marshaller xmlMarshaller;

    /** JAXB unmarshaller instance */
    private final Unmarshaller xmlUnmarshaller;

    /**
     * Database saving method
     *
     * @param databaseDataclass ticket database wrapper instance
     * @throws FileException if something went wrong during file operations
     * @throws DatabaseException if something went wrong during database operations
     */
    public void save(TicketDatabaseDataclass databaseDataclass)
            throws FileException, DatabaseException {
        fms.validateFileExists(savingFile);
        fms.validateFileIsWritable(savingFile);

        try {
            xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            xmlMarshaller.setAdapter(new LocalDateTimeAdapter());

            FileOutputStream fos = new FileOutputStream(savingFile);
            xmlMarshaller.marshal(databaseDataclass, fos);
        } catch (PropertyException e) {
            throw new UnableToSaveDatabaseException(
                    "It's unable to save the database. Marshaller property cannot be set");
        } catch (FileNotFoundException e) {
            throw new FileNotExistsException("Specified saving file does not exist");
        } catch (JAXBException e) {
            throw new UnableToSaveDatabaseException(
                    "It's unable to save the database. Something went wrong with marshaller");
        }
    }

    /**
     * Database loading method
     *
     * @return ticket database dataclass instance
     * @throws FileException if something went wrong during file operations
     * @throws DatabaseException if something went wrong during database operations
     */
    public TicketDatabaseDataclass load() throws FileException, DatabaseException {
        fms.validateFileExists(savingFile);
        fms.validateFileIsWritable(savingFile);

        try {
            xmlUnmarshaller.setAdapter(new LocalDateTimeAdapter());

            BufferedReader reader = new BufferedReader(new FileReader(savingFile));
            TicketDatabaseDataclass databaseDataclass =
                    (TicketDatabaseDataclass) xmlUnmarshaller.unmarshal(reader);

            if (databaseDataclass.getCreationDate() == null) {
                throw new UnableToLoadDatabaseException(
                        "Unable to load the database. Database creation date is incorrect.");
            }

            Set<Integer> usedTicketIds = new HashSet<>();
            Set<Long> usedVenueIds = new HashSet<>();
            List<Ticket> elements = databaseDataclass.getData();
            List<Ticket> correctElements = new ArrayList<>();

            for (Ticket element : elements) {
                if (element.getId() == null || usedTicketIds.contains(element.getId())) {
                    throw new UnableToLoadDatabaseException(
                            "Unable to load the database. Tickets should have unique ids");
                }

                if (element.getVenue() == null
                        || usedVenueIds.contains(element.getVenue().getId())) {
                    throw new UnableToLoadDatabaseException(
                            "Unable to load the database. Venues should have unique ids");
                }

                if (element.getVenue().getId() == 0) {
                    throw new UnableToLoadDatabaseException("Unable to load venue id");
                }

                try {
                    TicketValidator.validateTicket(element);
                    usedTicketIds.add(element.getId());
                    usedVenueIds.add(element.getVenue().getId());
                    correctElements.add(element);
                } catch (ValidationException e) {
                    throw new UnableToLoadDatabaseException(
                            "Unable to load the database. Some of the stored elements is corrupted");
                }
            }

            return new TicketDatabaseDataclass(
                    databaseDataclass.getCreationDate(), correctElements);
        } catch (FileNotFoundException e) {
            throw new FileNotExistsException("Specified saving file does not exist");
        } catch (JAXBException e) {
            throw new UnableToSaveDatabaseException(
                    "It's unable to load the database. Something went wrong with unmarshaller");
        }
    }
}
