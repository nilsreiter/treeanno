/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this FrameNet API.  If not, see www.gnu.org/licenses/gpl.html.
 * 
 */
package de.saar.coli.salsa.reiter.framenet;

import java.text.DateFormat;
import java.util.Date;

/**
 * This interface specifies that objects implementing it have an associated
 * cDate (creation date) in their XML file.
 * 
 * @author Nils Reiter
 * 
 */
public interface IHasCreationDate {

    /**
     * Returns the cDate as string.
     * 
     * @deprecated Use {@link #getCreationDate()} instead.
     * 
     * @return the cDate.
     */
    @Deprecated
    String getCDate();

    /**
     * The method returns the creation date of the resource as Date object. The
     * {@link DateFormat} object returned by {@link FrameNet#getDateFormat()}
     * can be used to create a String formatted as in the XML files.
     * 
     * @since 0.4.3
     * @return The creation date
     */
    Date getCreationDate();
}
