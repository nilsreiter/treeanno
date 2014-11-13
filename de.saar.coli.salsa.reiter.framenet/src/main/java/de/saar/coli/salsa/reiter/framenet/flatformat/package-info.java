package de.saar.coli.salsa.reiter.framenet.flatformat;

/**
 * 
 * Copyright 2007-2010 by Nils Reiter.
 * 
 * This FrameNet API is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; version 3.
 *
 * This FrameNet API is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this FrameNet API. If not, see www.gnu.org/licenses/gpl.html.
 * 
 * 
 * 
 * <body> This package is used to parse a flat and simple, plain-text based
 * format for storing annotated FrameNet data.
 * 
 * <h2>The format</h2>
 * 
 * This is an example annotation for the sentence
 * "Libya has shown interest in and taken steps to acquire weapons of mass destruction ( WMD ) and their delivery systems ."
 * . The sentence must be tokenized appropriately.
 * 
 * In the line after the sentence, several blocks of frame and fe annotations
 * can follow.
 * 
 * <pre>
 * text Libya has shown interest in and taken steps to acquire weapons of mass destruction ( WMD ) and their delivery systems .
 * frame Getting aquire
 * fe Theme weapons of mass destruction ( WMD ) and their delivery systems
 * fe Recipient Libya
 * frame Intentionally_act steps
 * fe Purpose to acquire weapons of mass destruction ( WMD ) and their delivery systems
 * fe Act steps
 * fe Agent Libya
 * frame Weapon weapons of mass destruction
 * fe Weapon weapons of mass destruction
 * fe Use of mass destruction
 * frame Gizmo systems
 * fe Gizmo systems
 * fe Use delivery
 * text ...
 * </pre>
 * 
 * </body>
 */
