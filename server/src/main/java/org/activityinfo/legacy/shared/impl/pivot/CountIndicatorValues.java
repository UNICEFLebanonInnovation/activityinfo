package org.activityinfo.legacy.shared.impl.pivot;

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

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.PivotSites;
import org.activityinfo.legacy.shared.command.PivotSites.ValueType;
import org.activityinfo.legacy.shared.impl.Tables;
import org.activityinfo.legacy.shared.model.IndicatorDTO;
import org.activityinfo.legacy.shared.reports.content.TargetCategory;

public class CountIndicatorValues extends BaseTable {

    @Override
    public boolean accept(PivotSites command) {
        return command.getValueType() == ValueType.INDICATOR;
    }

    @Override
    public void setupQuery(PivotSites command, SqlQuery query) {
        query.from(Tables.INDICATOR, "Indicator");
        query.leftJoin(Tables.ACTIVITY, "Activity").on("Activity.ActivityId=Indicator.ActivityId");
        query.leftJoin(Tables.SITE, "Site").on("Site.ActivityId=Activity.ActivityId");
        query.leftJoin(Tables.USER_DATABASE, "UserDatabase").on("UserDatabase.DatabaseId=Activity.DatabaseId");

        query.appendColumn("COUNT(DISTINCT Site.SiteId)", ValueFields.COUNT);
        query.appendColumn(Integer.toString(IndicatorDTO.AGGREGATE_SITE_COUNT), ValueFields.AGGREGATION);

        query.where("Indicator.Aggregation").equalTo(IndicatorDTO.AGGREGATE_SITE_COUNT);
        query.where("Indicator.dateDeleted").isNull();
        query.where("Site.dateDeleted").isNull();
    }

    @Override
    public String getDimensionIdColumn(DimensionType type) {
        switch (type) {
            case Partner:
                return "Site.PartnerId";
            case Activity:
                return "Site.ActivityId";
            case Site:
                return "Site.SiteId";
            case Project:
                return "Site.ProjectId";
            case Location:
                return "Site.LocationId";
            case Indicator:
                return "Indicator.IndicatorId";
            case Database:
                return "Activity.DatabaseId";
        }
        throw new UnsupportedOperationException(type.name());
    }

    @Override
    public String getDateCompleteColumn() {
        return "Site.Date2";
    }

    @Override
    public TargetCategory getTargetCategory() {
        return TargetCategory.REALIZED;
    }

}
