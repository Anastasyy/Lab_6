package com.megateam.common.data;

import com.megateam.common.data.util.LocalDateTimeAdapter;
import com.megateam.common.data.util.TicketType;

import lombok.*;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** This data class contains ticket information Is stored in the database */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ticket implements Comparable<Ticket> {
    /** This field contains unique ticket id */
    @Setter
    @XmlAttribute(name = "ticketId", required = true)
    private Integer id;

    /** This fields contains ticket name */
    @XmlAttribute(name = "ticketName", required = true)
    private final String name;

    /** This field contains ticket coordinates */
    @XmlElement(name = "ticketCoordinates", required = true)
    private final Coordinates coordinates;

    /** This field contains ticket creation date */
    @XmlElement(name = "ticketCreationDate", required = true)
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private final LocalDateTime creationDate;

    /** This field contains ticket price */
    @XmlElement(name = "ticketPrice", required = true)
    private final Float price;

    /** This field contains ticket comment */
    @XmlElement(name = "ticketComment", required = true)
    private final String comment;

    /** This field contains ticket refundable status */
    @XmlElement(name = "refundable", required = true)
    private final Boolean refundable;

    /** This field contains ticket type (can be null) */
    @XmlElement(name = "ticketType")
    private final TicketType type;

    /** This field contains ticket venue */
    @XmlElement(name = "ticketVenue", required = true)
    private final Venue venue;

    /**
     * This method returns a comparison result for this object and the specified one
     *
     * @param obj the object to be compared
     * @return comparison result (default comparison strategy with: less, equals and greater than
     *     zero)
     */
    @Override
    public int compareTo(Ticket obj) {
        if (this.equals(obj)) return 0;
        return this.name.compareTo(obj.name);
    }

    /**
     * This method provides access to a string representation of ticket class object
     *
     * @return ticket string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ticket id: ").append(id).append('\n');
        sb.append("Ticket name: ").append(name).append('\n');
        sb.append("Ticket coordinates: ").append('\t').append(coordinates).append('\n');
        sb.append("Ticket creation date: ").append(creationDate).append('\n');
        sb.append("Ticket price: ").append(price).append('\n');
        sb.append("Ticket comment: ").append(comment).append('\n');
        sb.append("Ticket refundable status: ").append(refundable).append('\n');
        sb.append("Ticket type: ").append((type == null) ? "not currently set" : type).append('\n');
        sb.append("Ticket venue: ").append('\t').append(venue).append('\n');

        return sb.toString();
    }
}
