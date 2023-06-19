package com.megateam.common.data;

import com.megateam.common.data.util.VenueType;

import lombok.*;

import javax.xml.bind.annotation.*;

/** This data class contains venue information Is used in Ticket data class */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@XmlRootElement(name = "venue")
@XmlAccessorType(XmlAccessType.FIELD)
public class Venue {
    /** This field contains unique venue id */
    @Setter
    @XmlAttribute(name = "venueId", required = true)
    private long id;

    /** This field contains venue name */
    @XmlElement(name = "venueName", required = true)
    private final String name;

    /** This field contains venue capacity */
    @XmlElement(name = "capacity", required = true)
    private final Integer capacity;

    /** This field contains venue type (can be null) */
    @XmlElement(name = "venueType")
    private final VenueType type;

    /**
     * This method provides access to a string representation of venue class object
     *
     * @return venue string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VenueID: ").append(id).append('\n');
        sb.append("Venue name: ").append(name).append('\n');
        sb.append("Venue capacity: ").append(capacity).append('\n');
        sb.append("Venue type: ").append((type == null) ? "not currently set" : type).append('\n');

        return sb.toString();
    }
}
