package org.activityinfo.ui.client.component.importDialog.validation;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.ResizeComposite;
import org.activityinfo.core.shared.importing.strategy.FieldImporterColumn;
import org.activityinfo.core.shared.importing.validation.ValidatedRow;
import org.activityinfo.core.shared.importing.validation.ValidatedRowTable;
import org.activityinfo.ui.client.style.table.DataGridResources;

/**
 * A second DataGrid that allows the user to the resolve
 * any problems before import
 */
public class ValidationGrid extends ResizeComposite {

    private DataGrid<ValidatedRow> dataGrid;

    public ValidationGrid() {
        this.dataGrid = new DataGrid<>(100, DataGridResources.INSTANCE);
        initWidget(dataGrid);
    }

    private void syncColumns() {

    }

    public void refresh(ValidatedRowTable table) {
        while (dataGrid.getColumnCount() > 0) {
            dataGrid.removeColumn(0);
        }
        for(int i = 0; i< table.getColumns().size(); i++) {
            final FieldImporterColumn column = table.getColumns().get(i);
            dataGrid.addColumn(new ValidationRowGridColumn(column.getAccessor(), i),
                    new TextHeader(column.getAccessor().getHeading()));
        }
        dataGrid.setRowData(table.getRows());
    }
}
