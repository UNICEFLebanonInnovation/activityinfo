package org.activityinfo.ui.client.page.entry;

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

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.client.Dispatcher;
import org.activityinfo.legacy.shared.command.*;
import org.activityinfo.legacy.shared.command.result.VoidResult;
import org.activityinfo.legacy.shared.model.ActivityDTO;
import org.activityinfo.legacy.shared.model.Published;
import org.activityinfo.legacy.shared.model.SchemaDTO;
import org.activityinfo.ui.client.page.entry.place.DataEntryPlace;

import java.util.Map;

public class EmbedDialog extends Dialog {

    private final Dispatcher dispatcher;
    private TextField<String> urlText;
    private TextField<String> embedText;

    public EmbedDialog(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        setWidth(300);
        setHeight(200);
        setHeadingText(I18N.CONSTANTS.embed());

        VBoxLayout layout = new VBoxLayout();
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        layout.setPadding(new Padding(10));
        setLayout(layout);

        add(new Label("Paste link:"));
        urlText = new TextField<String>();
        urlText.setReadOnly(true);
        urlText.setSelectOnFocus(true);
        add(urlText);

        add(new Label(" "));
        add(new Label("Paste HTML to embed into websites:"));

        embedText = new TextField<String>();
        embedText.setReadOnly(true);
        embedText.setSelectOnFocus(true);
        add(embedText);
    }

    public void show(String url) {
        urlText.setValue(url);
        embedText.setValue("<iframe src=\"" + url + "\" width=\"400\" height=\"200\"></iframe>");
        super.show();
    }

    public void show(final DataEntryPlace place) {
        final String url =
                "http://www.activityinfo.org/embed.html?sites=" + FilterUrlSerializer.toUrlFragment(place.getFilter());

        dispatcher.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(SchemaDTO result) {
                Filter filter = place.getFilter();
                if (filter.isDimensionRestrictedToSingleCategory(DimensionType.Activity)) {
                    ActivityDTO singleActivity = result.getActivityById(filter.getRestrictedCategory(DimensionType
                            .Activity));
                    showPublished(singleActivity, url);
                } else if (filter.isDimensionRestrictedToSingleCategory(DimensionType.Database)) {
                    MessageBox.alert("foo", "not impl", null);
                }
            }
        });
    }

    private void showPublished(final ActivityDTO activity, final String url) {
        if (activity.getPublished() != Published.ALL_ARE_PUBLISHED.getIndex()) {
            if (activity.isDesignAllowed()) {
                MessageBox.confirm(I18N.CONSTANTS.embed(),
                        I18N.MESSAGES.promptPublishActivity(activity.getName()),
                        new Listener<MessageBoxEvent>() {
                            @Override
                            public void handleEvent(MessageBoxEvent be) {
                                if (be.getButtonClicked().getItemId().equals(Dialog.YES)) {
                                    publishActivity(activity, url);
                                }
                            }
                        });
            } else {
                MessageBox.alert(I18N.CONSTANTS.embed(), I18N.MESSAGES.activityNotPublic(activity.getName()), null);
            }
        } else {
            show(url);
        }
    }

    protected void publishActivity(ActivityDTO activity, final String url) {
        Map<String, Object> changes = Maps.newHashMap();
        changes.put("published", Published.ALL_ARE_PUBLISHED.getIndex());

        UpdateEntity update = new UpdateEntity(activity, changes);
        dispatcher.execute(update, new AsyncCallback<VoidResult>() {

            @Override
            public void onFailure(Throwable caught) {
                MessageBox.alert(I18N.CONSTANTS.embed(),
                        "There was an error encounted on the server while trying to publish the activity",
                        null);
            }

            @Override
            public void onSuccess(VoidResult result) {
                show(url);
            }
        });
    }

}
