package com.megateam.server.database;

import com.megateam.common.data.util.TicketType;
import com.megateam.common.exception.impl.database.ElementIdAlreadyExistsException;
import com.megateam.common.exception.impl.database.ElementNotFoundException;
import com.megateam.common.exception.impl.database.UnableToLoadDatabaseException;
import com.megateam.common.exception.impl.database.UnableToSaveDatabaseException;

import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

/** An abstraction for a file database */
public interface Database<T> {
    /**
     * This is an abstraction for method retrieves amount of stored elements
     *
     * @return amount of stored elements
     */
    int size();

    /**
     * This is an abstraction for method retrieves creation date of the database
     *
     * @return database creation date
     */
    LocalDateTime getCreationDate();

    /**
     * This is an abstraction for method that retrieves all elements from the database
     *
     * @return list of stored elements
     */
    List<T> findAll();

    /**
     * This is an abstraction for method that is retrieving stored element in the database by id
     *
     * @param id stored element id
     * @return element
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    T findElementById(@NonNull Integer id) throws ElementNotFoundException;

    /**
     * This is an abstraction for method that provides an ability to add new element into the
     * database
     *
     * @param item the new element which should be added into the database
     * @throws ElementIdAlreadyExistsException if trying to add new element with existing id
     */
    void add(@NonNull T item) throws ElementIdAlreadyExistsException;

    /**
     * This is an abstraction for method that provides an ability to update the existing element by
     * id
     *
     * @param id existing element id
     * @param item element that the existing element will be updated with
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    void update(@NonNull Integer id, @NonNull T item) throws ElementNotFoundException;

    /**
     * This is an abstraction for method that removes an element from the database by id
     *
     * @param id element id
     * @throws ElementNotFoundException if the specified id does not exist in the database
     */
    void remove(@NonNull Integer id) throws ElementNotFoundException;

    /** This is an abstraction for method that clears the database */
    void clear();

    /**
     * This is an abstraction for method that saves the collection to a file
     *
     * @throws UnableToSaveDatabaseException if something went wrong during the database saving
     */
    void save() throws UnableToSaveDatabaseException;

    /**
     * This is an abstraction for method that loads the collection to a file
     *
     * @throws UnableToLoadDatabaseException if something went wrong during the database loading
     */
    void load() throws UnableToLoadDatabaseException;

    /** This method is an abstraction for method that inits an empty database */
    void initEmptyDb();

    /**
     * This is an abstraction for method that removes the first element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    void removeFirst() throws ElementNotFoundException;

    /**
     * This is an abstraction for method that removes the last element from the database
     *
     * @throws ElementNotFoundException if required element not found
     */
    void removeLast() throws ElementNotFoundException;

    /**
     * This is an abstraction for method that removes element which is lower than specified one
     *
     * @param item an element for the comparison
     * @throws ElementNotFoundException if required element not found
     */
    void removeLower(@NonNull T item) throws ElementNotFoundException;

    /**
     * This is an abstraction for method that removes all the elements with the refundable status
     * specified
     *
     * @param refundable refundable status of removable elements
     * @throws ElementNotFoundException if it's nothing to remove
     */
    void removeAnyByRefundable(@NonNull Boolean refundable) throws ElementNotFoundException;

    /**
     * This is an abstraction for method that retrieves all the elements with type less than
     * specified
     *
     * @param type type to filter
     * @return list of filtered elements
     */
    List<T> findLessThanType(@NonNull TicketType type);
}
