package org.activityinfo.server.command;

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

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.activityinfo.legacy.shared.command.CreateLocation;
import org.activityinfo.legacy.shared.command.CreateSite;
import org.activityinfo.legacy.shared.command.GetSites;
import org.activityinfo.legacy.shared.command.exception.NotAuthorizedException;
import org.activityinfo.legacy.shared.command.result.CreateResult;
import org.activityinfo.legacy.shared.exception.CommandException;
import org.activityinfo.legacy.shared.model.*;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.legacy.client.KeyGenerator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.GregorianCalendar;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class CreateSiteTest extends CommandTestCase2 {

    @Test
    public void test() throws CommandException {
        LocationDTO location = LocationDTOs.newLocation();
        execute(new CreateLocation(location));

        SiteDTO newSite = SiteDTOs.newSite();
        newSite.setLocation(location);

        CreateSite cmd = new CreateSite(newSite);
        setUser(1);

        CreateResult result = execute(cmd);
        newSite.setId(result.getNewId());
        assertThat(result.getNewId(), not(equalTo(0)));
        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite
                .getId()));
        Assert.assertEquals(1, loadResult.getData().size());
        SiteDTO secondRead = loadResult.getData().get(0);
        SiteDTOs.validateNewSite(secondRead);
    }

    @Test(expected = NotAuthorizedException.class)
    public void unauthorized() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = SiteDTOs.newSite();
        newSite.setPartner(new PartnerDTO(2, "Not NRC"));
        // create command
        CreateSite cmd = new CreateSite(newSite);

        // execute the command

        setUser(2); // bavon (only has access to his own partners in database 1)
        execute(cmd);
    }

    @Test
    @Ignore("WIP")
    public void testAdminBoundCreate() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();

        newSite.setActivityId(4);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setAdminEntity(1, new AdminEntityDTO(1, 2, "Sud Kivu"));
        newSite.setAdminEntity(2, new AdminEntityDTO(2, 11, "Walungu"));
        newSite.setAdminEntity(3, null);
        newSite.setX(27.432);
        newSite.setY(1.23);
        newSite.setComments("huba huba");
        newSite.setProject(new ProjectDTO(1, "SomeProject"));

        // create command

        CreateSite cmd = new CreateSite(newSite);

        // execute the command

        setUser(1);
        newSite.setProject(new ProjectDTO(1, "SomeProject"));

        CreateResult result = execute(cmd);
        newSite.setId(result.getNewId());

        // try to retrieve what we've created

        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite
                .getId()));

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);

        // confirm that the changes are there
        Assert.assertEquals("site.location.name", "Walungu",
                secondRead.getLocationName());
    }

    @Test
    public void testAllAttribsFalse() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();
        newSite.setId(new KeyGenerator().generateInt());
        newSite.setActivityId(1);
        newSite.setLocationId(1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setLocationName("Virunga");
        newSite.setAttributeValue(1, false);
        newSite.setAttributeValue(2, false);
        newSite.setProject(new ProjectDTO(1, "SomeProject"));

        // create command

        CreateSite cmd = new CreateSite(newSite);
        assertThat((Integer) cmd.getProperties().get("locationId"), equalTo(1));

        // execute the command

        setUser(1);

        CreateResult result = execute(cmd);

        // let the client know the command has succeeded
        newSite.setId(result.getNewId());
        // cmd.onCompleted(result);

        // try to retrieve what we've created

        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite
                .getId()));

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);

        // confirm that the changes are there
        Assert.assertEquals("site.attribute[2]", false,
                secondRead.getAttributeValue(1));
        Assert.assertEquals("site.attribute[2]", false,
                secondRead.getAttributeValue(2));
    }

}
