package org.activityinfo.server.report;

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

import com.google.inject.Inject;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.fixtures.MockHibernateModule;
import org.activityinfo.fixtures.Modules;
import org.activityinfo.fixtures.ServletStubModule;
import org.activityinfo.legacy.shared.command.RenderElement;
import org.activityinfo.legacy.shared.reports.content.PivotContent;
import org.activityinfo.legacy.shared.reports.model.MapReportElement;
import org.activityinfo.legacy.shared.reports.model.Report;
import org.activityinfo.legacy.shared.reports.model.ReportElement;
import org.activityinfo.legacy.shared.reports.model.TableElement;
import org.activityinfo.legacy.shared.reports.model.labeling.ArabicNumberSequence;
import org.activityinfo.legacy.shared.reports.model.layers.BubbleMapLayer;
import org.activityinfo.legacy.shared.reports.model.layers.ScalingType;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.ReportGenerator;
import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.server.report.renderer.RendererFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(InjectionSupport.class)
@Modules({ReportStubModule.class, ServletStubModule.class,
        MockHibernateModule.class})
public class ReportsTest {

    @Inject
    private EntityManager em;

    @Inject
    private ReportGenerator reportGenerator;

    @Inject
    private RendererFactory factory;

    @Inject
    private User user;

    @Before
    public void setup() {
        user = createUser();
        createDirectoriesIfNecessary();
    }

    @Test
    @Ignore("need dbunit xml with matching data")
    public void testFullReport() throws Throwable {
        Report report = getReport("full-test.xml");

        reportGenerator.generate(user, report, null, null);
        for (RenderElement.Format format : RenderElement.Format.values()) {
            if (format != RenderElement.Format.Excel
                    && format != RenderElement.Format.Excel_Data) {
                Renderer renderer = factory.get(format);
                FileOutputStream fos = new FileOutputStream(
                        "target/report-tests/full-test" + renderer.getFileSuffix());
                renderer.render(report, fos);
                fos.close();
            }
        }
    }

    @Test
    public void testApplesReport() throws Throwable {
        Report report = getReport("realworld/ApplesReport.xml");

        reportGenerator.generate(user, report, null, null);

        ReportElement element = report.getElements().get(0);
        element.getContent();

        assertEquals("Expected different report title", report.getTitle(),
                "Phase one apple report");

        assertEquals("Expected different report description",
                report.getDescription(),
                "Apples come in different shapes, colors and taste");

        assertEquals("Expected only one filter",
                report.getFilter().getRestrictedDimensions().size(), 1);

        assertEquals("Expected one element", report.getElements().size(), 1);

        assertTrue("Expected pivottable element",
                element.getContent() instanceof PivotContent);

        assertEquals("Expected title: 'Apples, bananas and oranges'",
                element.getTitle(), "Apples, bananas and oranges");
    }

    @Test
    public void testConsolideDesActivitesReport() throws Throwable {

        // Setup test
        Report report = getReport("realworld/ConsolideDesActivites.xml");

        reportGenerator.generate(user, report, null, null);

        TableElement pivotTable = (TableElement) report.getElements().get(2);
        MapReportElement map1 = pivotTable.getMap();
        BubbleMapLayer bubbleMap = (BubbleMapLayer) map1.getLayers().get(0);

        assertTrue("Arabic numbering expected",
                bubbleMap.getLabelSequence() instanceof ArabicNumberSequence);

        assertEquals("MinRadius of 8 expected", bubbleMap.getMinRadius(), 8);
        assertEquals("MaxRadius of 14 expected", bubbleMap.getMaxRadius(), 14);
        assertEquals("Graduated scaling expected", bubbleMap.getScaling(),
                ScalingType.Graduated);
    }

    private void createDirectoriesIfNecessary() {
        File file = new File("target/report-test");
        file.mkdirs();
    }

    private User createUser() {
        User u = new User();
        u.setId(3); // akbertram
        return u;
        // return (User)
        // em.createQuery("select u from User u where u.email = :email")
        // .setParameter("email", "akbertram@gmail.com").getResultList().get(0);
    }

    public Report getReport(String reportNameWithPath) throws JAXBException {
        return ReportParserJaxb
                .parseXML(new InputStreamReader(
                        getClass().getResourceAsStream(
                                "/report-def/" + reportNameWithPath)));
    }

    public Report getReport(int id) {
        return null;
    }
}
