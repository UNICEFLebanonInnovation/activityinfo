package org.activityinfo.server.database.hibernate.entity;

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

import org.activityinfo.legacy.shared.reports.model.EmailDelivery;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines a subscription to a given report.
 *
 * @author Alex Bertram
 */
@Entity
public class ReportSubscription implements Serializable {

    private ReportSubscriptionId id;
    private ReportDefinition template;
    private User user;
    private boolean subscribed;
    private User invitingUser;
    private EmailDelivery emailDelivery;
    private Integer emailDay;

    public ReportSubscription() {
    }

    public ReportSubscription(ReportDefinition template, User user) {
        id = new ReportSubscriptionId(template.getId(), user.getId());
        this.template = template;
        this.user = user;
    }

    @EmbeddedId
    @AttributeOverrides({@AttributeOverride(name = "reportId", column = @Column(name = "reportId", nullable = false)),
            @AttributeOverride(name = "userId", column = @Column(name = "userId", nullable = false))})
    public ReportSubscriptionId getId() {
        return this.id;
    }

    public void setId(ReportSubscriptionId id) {
        this.id = id;
    }

    /**
     * Gets the ReportTemplate to which the user is subscribed
     *
     * @return the ReportTemplate to which the user is subscribed
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId", nullable = false, insertable = false, updatable = false)
    public ReportDefinition getTemplate() {
        return this.template;
    }

    /**
     * Sets the Report Template to which the user is subscribed.
     *
     * @param template The report template
     */
    public void setTemplate(ReportDefinition template) {
        this.template = template;
    }

    /**
     * Get the user who will receive the report by mail.
     *
     * @return The user to whom the report will be mailed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who will receive the report by mail.
     *
     * @param user The user who will receive the report by mail.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the inviting user
     *
     * @return The second user who has invited <code>user</code> to subscribe to
     * this report. NULL if the user has set their own preferences.
     */
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "invitingUserId", nullable = true)
    public User getInvitingUser() {
        return invitingUser;
    }

    /**
     * Sets the inviting user
     *
     * @param invitingUser A second user who has invited the <code>user</code> to
     *                     subscribe to the report.
     */
    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    /**
     * Gets the subscription status to <code>report</code>
     *
     * @return True if the user is subscribed to the <code>report</code>
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Sets the subscription status to <code>report</code>
     *
     * @param subscribed True if the user is to receive this report by mail.
     */
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    @Enumerated(EnumType.STRING)
    public EmailDelivery getEmailDelivery() {
        return emailDelivery;
    }

    public void setEmailDelivery(EmailDelivery frequency) {
        this.emailDelivery = frequency;
    }

    @Column(nullable = true)
    public Integer getEmailDay() {
        return emailDay;
    }

    public void setEmailDay(Integer day) {
        this.emailDay = day;
    }

}
