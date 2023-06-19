package com.megateam.server.dao;

import com.megateam.common.dao.Dao;
import com.megateam.common.data.Ticket;
import com.megateam.common.data.util.TicketType;
import com.megateam.common.exception.impl.database.ElementIdAlreadyExistsException;
import com.megateam.common.exception.impl.database.ElementNotFoundException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;
import com.megateam.server.database.Database;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/** An implementation of dao for interacting with ticket database */
@RequiredArgsConstructor
public class TicketDaoImpl implements Dao<Ticket> {
    /** Ticket database instance */
    private final Database<Ticket> database;

    /**
     * This method retrieves amount of stored elements
     *
     * @return amount of stored elements
     */
    @Override
    public int size() {
        return database.size();
    }

    /**
     * This method retrieves creation date of the database
     *
     * @return database creation date
     */
    @Override
    public LocalDateTime getCreationDate() {
        return database.getCreationDate();
    }

    /**
     * This method retrieves all elements from the database
     *
     * @return list of stored elements
     */
    @Override
    public List<Ticket> findAll() {
        return database.findAll();
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
        return database.findElementById(id);
    }

    /**
     * This method provides an ability to add new element into the database
     *
     * @param item the new element which should be added into the database
     */
    @Override
    public void add(@NonNull Ticket item) throws ElementIdAlreadyExistsException {
        database.add(item);
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
        database.update(id, item);
    }

    /**
     * This method removes an element from the database by id
     *
     * @param id element id
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    @Override
    public void remove(@NonNull Integer id) throws ElementNotFoundException {
        database.remove(id);
    }

    /** This method clears the database */
    @Override
    public void clear() {
        database.clear();
    }

    /**
     * This method saves the collection to a file
     *
     * @throws UnableToSaveDatabaseException if something went wrong during the database saving
     */
    @Override
    public void save() throws UnableToSaveDatabaseException {
        database.save();
    }

    /**
     * This method removes the first element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeFirst() throws ElementNotFoundException {
        database.removeFirst();
    }

    /**
     * This method removes the last element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeLast() throws ElementNotFoundException {
        database.removeLast();
    }

    /**
     * This method removes element which is lower than specified one
     *
     * @param item an element for the comparison
     * @throws ElementNotFoundException if required element not found
     */
    @Override
    public void removeLower(@NonNull Ticket item) throws ElementNotFoundException {
        database.removeLower(item);
    }

    /**
     * This method removes all the elements with the refundable status specified
     *
     * @param refundable refundable status of removable elements
     * @throws ElementNotFoundException if it's nothing to remove
     */
    @Override
    public void removeAnyByRefundable(@NonNull Boolean refundable) throws ElementNotFoundException {
        database.removeAnyByRefundable(refundable);
    }

    /**
     * This method retrieves all the elements with type less than specified
     *
     * @param type type to filter
     * @return list of filtered elements
     */
    @Override
    public List<Ticket> findLessThanType(@NonNull TicketType type) {
        return database.findLessThanType(type);
    }
}
