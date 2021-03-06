package org.activityinfo.ui.client.page.entry.sitehistory;

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

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.command.Month;
import org.activityinfo.legacy.shared.model.AttributeDTO;
import org.activityinfo.legacy.shared.model.IndicatorDTO;
import org.activityinfo.legacy.shared.model.LocationDTO;
import org.activityinfo.legacy.shared.model.PartnerDTO;
import org.activityinfo.legacy.shared.model.ProjectDTO;
import org.activityinfo.legacy.shared.model.SchemaDTO;

import java.util.Map;

class ItemDetail {
    private String stringValue;

    static ItemDetail create(RenderContext ctx, Map.Entry<String, Object> entry) {
        ItemDetail d = new ItemDetail();

        Map<String, Object> state = ctx.getState();
        SchemaDTO schema = ctx.getSchema();

        String key = entry.getKey();
        final Object oldValue = state.get(key);
        final Object newValue = entry.getValue();
        state.put(key, newValue);

        final StringBuilder sb = new StringBuilder();

        // basic
        if (key.equals("date1")) {
            addValues(sb, I18N.CONSTANTS.startDate(), oldValue, newValue);

        } else if (key.equals("date2")) {
            addValues(sb, I18N.CONSTANTS.endDate(), oldValue, newValue);

        } else if (key.equals("comments")) {
            addValues(sb, I18N.CONSTANTS.comments(), oldValue, newValue);

        } else if (key.equals("locationId")) { // schema loookups
            String oldName = null;
            if (oldValue != null) {
                LocationDTO location = ctx.getLocation(toInt(oldValue));
                if (location != null) {
                    oldName = location.getName();
                }
            }
            String newName = ctx.getLocation(toInt(newValue)).getName();
            addValues(sb, I18N.CONSTANTS.location(), oldName, newName);

        } else if (key.equals("projectId")) {
            String oldName = null;
            if (oldValue != null) {
                ProjectDTO project = schema.getProjectById(toInt(oldValue));
                if (project != null) {
                    oldName = project.getName();
                }
            }
            String newName = schema.getProjectById(toInt(newValue)).getName();
            addValues(sb, I18N.CONSTANTS.project(), oldName, newName);

        } else if (key.equals("partnerId")) {
            String oldName = null;
            if (oldValue != null) {
                PartnerDTO oldPartner = schema.getPartnerById(toInt(oldValue));
                if (oldPartner != null) {
                    oldName = oldPartner.getName();
                }
            }
            PartnerDTO newPartner = schema.getPartnerById(toInt(newValue));
            if (newPartner != null) {
                String newName = newPartner.getName();
                addValues(sb, I18N.CONSTANTS.partner(), oldName, newName);
            }

        } else if (key.startsWith(IndicatorDTO.PROPERTY_PREFIX)) {
            // custom
            int id = IndicatorDTO.indicatorIdForPropertyName(key);
            IndicatorDTO dto = schema.getIndicatorById(id);
            if (dto != null) {
                String name = dto.getName();

                Month m = IndicatorDTO.monthForPropertyName(key);
                if (m != null) {
                    name = I18N.MESSAGES.siteHistoryIndicatorName(name, m.toLocalDate().atMidnightInMyTimezone());
                }

                addValues(sb, name, oldValue, newValue, dto.getUnits());
            }

        } else if (key.startsWith(AttributeDTO.PROPERTY_PREFIX)) {
            int id = AttributeDTO.idForPropertyName(key);
            AttributeDTO dto = schema.getAttributeById(id);
            if (dto != null) {
                if (Boolean.parseBoolean(newValue.toString())) {
                    sb.append(I18N.MESSAGES.siteHistoryAttrAdd(dto.getName()));
                } else {
                    sb.append(I18N.MESSAGES.siteHistoryAttrRemove(dto.getName()));
                }
            }

        } else {
            // fallback
            addValues(sb, key, oldValue, newValue);
        }

        d.stringValue = sb.toString();

        return d;
    }

    private static void addValues(StringBuilder sb, String key, Object oldValue, Object newValue) {
        addValues(sb, key, oldValue, newValue, null);
    }

    private static void addValues(StringBuilder sb, String key, Object oldValue, Object newValue, String units) {
        sb.append(key);
        sb.append(": ");
        sb.append(newValue);

        if (units != null) {
            sb.append(" ");
            sb.append(units);
        }

        if (!equals(oldValue, newValue)) {
            sb.append(" (");
            if (oldValue == null) {
                sb.append(I18N.MESSAGES.siteHistoryOldValueBlank());
            } else {
                sb.append(I18N.MESSAGES.siteHistoryOldValue(oldValue));
            }
            sb.append(")");
        }
    }

    private static boolean equals(Object oldValue, Object newValue) {
        if (oldValue == newValue) {
            return true;
        }
        if ((oldValue == null) || (newValue == null)) {
            return false;
        }
        return oldValue.equals(newValue);
    }

    private static int toInt(Object val) {
        return val != null ? Integer.parseInt(val.toString()) : -1;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
