package com.megateam.server.database.data;

import com.megateam.common.data.Ticket;
import com.megateam.common.data.util.LocalDateTimeAdapter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/** Container class for saving ticket database */
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "database")
public class TicketDatabaseDataclass {
    /** Collection creation date */
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name = "dbCreationDate", required = true)
    private final LocalDateTime creationDate;

    /** Collection data */
    @XmlElementWrapper(name = "elements", required = true)
    @XmlElement(name = "element")
    private final List<Ticket> data;
}
