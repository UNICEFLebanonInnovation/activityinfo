package org.activityinfo.ui.full.client.widget.form;
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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import org.activityinfo.api2.shared.Cuid;
import org.activityinfo.api2.shared.form.FormInstance;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author yuriyz on 2/10/14.
 */
public class FormFieldWidgetReferenceSuggestBox extends Composite implements FormFieldWidget<List<Cuid>> {

    private final SuggestBox suggestBox;
    private final List<FormInstance> instances;
    private final Map<String, Cuid> labelToCuidMap = Maps.newHashMap();

    public FormFieldWidgetReferenceSuggestBox(List<FormInstance> instances) {
        this.instances = instances;
        suggestBox = createSuggestBox();
        initWidget(suggestBox);
    }

    private SuggestBox createSuggestBox() {
        final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        for (FormInstance instance : instances) {
            final String labelValue = instance.getLabel().getValue();
            oracle.add(labelValue);
            labelToCuidMap.put(labelValue, instance.getId());
        }
        return new SuggestBox(oracle);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        suggestBox.setEnabled(!readOnly);
    }

    @Override
    public boolean isReadOnly() {
        return !suggestBox.isEnabled();
    }

    @Override
    public List<Cuid> getValue() {
        final Cuid cuid = labelToCuidMap.get(suggestBox.getValue());
        if (cuid != null) {
            return Lists.newArrayList(cuid);
        }
        return Lists.newArrayList();
    }

    @Override
    public void setValue(List<Cuid> value) {
        setValue(value, false);
    }

    @Override
    public void setValue(List<Cuid> value, boolean fireEvents) {
        if (value != null && !value.isEmpty()) {
            final List<Cuid> oldValue = getValue();
            final Cuid cuid = value.iterator().next();
            final FormInstance formInstance = Iterables.find(instances, new Predicate<FormInstance>() {
                @Override
                public boolean apply(@Nullable FormInstance input) {
                    return input != null && cuid.equals(input.getId());
                }
            });
            suggestBox.setValue(formInstance.getLabel().getValue());
            if (fireEvents) {
                CuidValueChangeEvent.fireIfNotEqual(this, oldValue, value);
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<Cuid>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
