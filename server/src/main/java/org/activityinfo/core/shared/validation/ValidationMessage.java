package org.activityinfo.core.shared.validation;
/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.common.base.Strings;

import java.lang.String;

/**
 * @author yuriyz on 3/11/14.
 */
public class ValidationMessage {

    private String message;
    private ValidationSeverity severity;

    public ValidationMessage() {
        this("");
    }

    public ValidationMessage(String message) {
        this(message, ValidationSeverity.ERROR);
    }


    public ValidationMessage(String message, ValidationSeverity severity) {
        this.message = message;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ValidationSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ValidationSeverity severity) {
        this.severity = severity;
    }

    @Override
    public String toString() {
        return "ValidationMessage{" +
                "message=" + message +
                ", severity=" + severity +
                '}';
    }
}

