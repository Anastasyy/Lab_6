package com.megateam.server.database;

import com.megateam.common.data.Coordinates;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.Venue;
import com.megateam.common.data.util.TicketIdGenerator;
import com.megateam.common.data.util.TicketType;
import com.megateam.common.data.util.VenueIdGenerator;
import com.megateam.common.data.util.VenueType;
import com.megateam.common.exception.DatabaseException;
import com.megateam.common.exception.FileException;
import com.megateam.common.exception.impl.database.ElementIdAlreadyExistsException;
import com.megateam.common.exception.impl.database.ElementNotFoundException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;
import com.megateam.server.database.data.TicketDatabaseDataclass;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/** Database implementation for storing tickets */
public class TicketDatabaseImpl implements Database<Ticket> {
    /** The database creation date */
    private LocalDateTime creationDate;

    /** A storage for all the tickets */
    private List<Ticket> tickets;

    /** Database saving service instance */
    private final DatabaseSavingService savingService;

    /**
     * TicketDatabaseImpl constructor
     *
     * @param savingService database saving service instance
     */
    public TicketDatabaseImpl(DatabaseSavingService savingService) {
        initEmptyDb();
        this.savingService = savingService;
    }

    /**
     * This method retrieves amount of stored elements
     *
     * @return amount of stored elements
     */
    @Override
    public int size() {
        return tickets.size();
    }

    /**
     * This method retrieves creation date of the database
     *
     * @return database creation date
     */
    @Override
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * This method retrieves all elements from the database
     *
     * @return list of stored elements
     */
    @Override
    public List<Ticket> findAll() {
        return tickets;
    }

    /**
     * This method retrieves element stored in the database by id
     *
     * @param id stored element id
     * @return element
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    @Override
    public Ticket findElementById(@NonNull Integer id) throws ElementNotFoundException {
        Optional<Ticket> optionalTicket =
                tickets.stream().filter((ticket) -> id.equals(ticket.getId())).findFirst();

        if (optionalTicket.isEmpty())
            throw new ElementNotFoundException("Element with specified id not found");

        return optionalTicket.get();
    }

    /**
     * This method provides an ability to add new element into the database
     *
     * @param item the new element which should be added into the database
     */
    @Override
    public void add(@NonNull Ticket item) throws ElementIdAlreadyExistsException {
        item.setId(TicketIdGenerator.generateNewId());
        item.getVenue().setId(VenueIdGenerator.generateNewId());

        tickets.add(item);
    }

    /**
     * This method provides an ability to update the existing element by id
     *
     * @param id existing element id
     * @param item element that the existing element will be updated with
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    @Override
    public void update(@NonNull Integer id, @NonNull Ticket item) throws ElementNotFoundException {
        Optional<Ticket> optionalTicket =
                tickets.stream().filter(ticket -> id.equals(ticket.getId())).findFirst();

        if (optionalTicket.isEmpty())
            throw new ElementNotFoundException("Element with id " + id + " not found");

        Ticket existingTicket = optionalTicket.get();

        String ticketName = (item.getName() == null) ? existingTicket.getName() : item.getName();
        Float xCoord =
                (item.getCoordinates().getX() == null)
                        ? existingTicket.getCoordinates().getX()
                        : item.getCoordinates().getX();
        Integer yCoord =
                (item.getCoordinates().getY() == null)
                        ? existingTicket.getCoordinates().getY()
                        : item.getCoordinates().getY();
        LocalDateTime ticketCreationDate = existingTicket.getCreationDate();
        Float price = (item.getPrice() == null) ? existingTicket.getPrice() : item.getPrice();
        String comment =
                (item.getComment() == null) ? existingTicket.getComment() : item.getComment();
        Boolean refundable =
                (item.getRefundable() == null)
                        ? existingTicket.getRefundable()
                        : item.getRefundable();
        TicketType ticketType =
                (item.getType() == null) ? existingTicket.getType() : item.getType();

        String venueName =
                (item.getVenue().getName() == null)
                        ? existingTicket.getVenue().getName()
                        : item.getVenue().getName();
        Integer venueCapacity =
                (item.getVenue().getCapacity() == null)
                        ? existingTicket.getVenue().getCapacity()
                        : item.getVenue().getCapacity();
        VenueType venueType =
                (item.getVenue().getType() == null)
                        ? existingTicket.getVenue().getType()
                        : item.getVenue().getType();

        Coordinates coordinates = new Coordinates(xCoord, yCoord);
        Venue venue =
                new Venue(existingTicket.getVenue().getId(), venueName, venueCapacity, venueType);
        Ticket newTicket =
                new Ticket(
                        id,
                        ticketName,
                        coordinates,
                        ticketCreationDate,
                        price,
                        comment,
                        refundable,
                        ticketType,
                        venue);

        tickets.remove(existingTicket);
        tickets.add(newTicket);
    }

    /**
     * This method removes an element from the database by id
     *
     * @param id element id
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    @Override
    public void remove(@NonNull Integer id) throws ElementNotFoundException {
        Iterator<Ticket> iterator = tickets.iterator();

        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();

            if (id.equals(ticket.getId())) {
                iterator.remove();
                return;
            }
        }

        throw new ElementNotFoundException("Element with id " + id + " not found");
    }

    /** This method clears the database */
    @Override
    public void clear() {
        tickets.clear();
    }

    /**
     * This method saves the collection to a file
     *
     * @throws UnableToSaveDatabaseException if something went wrong during the database saving
     */
    @Override
    public void save() throws UnableToSaveDatabaseException {
        try {
            savingService.save(new TicketDatabaseDataclass(creationDate, tickets));
        } catch (FileException | DatabaseException e) {
            throw new UnableToSaveDatabaseException(e.getMessage());
        }
    }

    /**
     * This method loads the collection to a file
     *
     * @throws UnableToLoadDatabaseException if something went wrong during the database loading
     */
    @Override
    public void load() throws UnableToLoadDatabaseException {
        try {
            TicketDatabaseDataclass dataclass = savingService.load();
            this.creationDate = dataclass.getCreationDate();
            this.tickets = dataclass.getData();
        } catch (FileException | DatabaseException e) {
            throw new UnableToLoadDatabaseException(e.getMessage());
        }
    }

    @Override
    public void initEmptyDb() {
        this.creationDate = LocalDateTime.now(ZoneId.systemDefault());
        this.tickets = new ArrayList<>();
    }

    /**
     * This method removes the first element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeFirst() throws ElementNotFoundException {
        if (tickets.size() == 0)
            throw new ElementNotFoundException(
                    "There are no elements in the database. Cannot remove first");

        tickets.remove(0);
    }

    /**
     * This method removes the last element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeLast() throws ElementNotFoundException {
        if (tickets.size() == 0)
            throw new ElementNotFoundException(
                    "There are no elements in the database. Cannot remove last");

        tickets.remove(tickets.size() - 1);
    }

    /**
     * This method removes element which is lower than specified one
     *
     * @param item an element for the comparison
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeLower(@NonNull Ticket item) throws ElementNotFoundException {
        List<Integer> removedIds =
                tickets.stream()
                        .filter(ticket -> ticket.compareTo(item) < 0)
                        .map(Ticket::getId)
                        .toList();

        for (Integer id : removedIds) {
            remove(id);
        }
    }

    /**
     * This method removes all the elements with the refundable status specified
     *
     * @param refundable refundable status of removable elements
     * @throws ElementNotFoundException if it's nothing to remove
     */
    @Override
    public void removeAnyByRefundable(@NonNull Boolean refundable) throws ElementNotFoundException {
        List<Integer> ticketsIDsToRemove =
                tickets.stream()
                        .filter(
                                ticket ->
                                        ticket.getRefundable().booleanValue()
                                                == refundable.booleanValue())
                        .map(Ticket::getId)
                        .toList();

        if (ticketsIDsToRemove.size() == 0)
            throw new ElementNotFoundException("There are no elements to be removed");

        for (Integer id : ticketsIDsToRemove) {
            remove(id);
        }
    }

    /**
     * This method retrieves all the elements with type less than specified
     *
     * @param type type to filter
     * @return list of filtered elements
     */
    @Override
    public List<Ticket> findLessThanType(@NonNull TicketType type) {
        return tickets.stream()
                .filter(
                        ticket ->
                                (ticket.getType() == null || ticket.getType().compareTo(type) > 0))
                .toList();
    }
}
