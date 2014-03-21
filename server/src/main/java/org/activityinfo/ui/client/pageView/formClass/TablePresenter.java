package org.activityinfo.ui.client.pageView.formClass;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.core.client.ResourceLocator;
import org.activityinfo.core.shared.Cuid;
import org.activityinfo.core.shared.criteria.ClassCriteria;
import org.activityinfo.core.shared.form.tree.FormTree;
import org.activityinfo.fp.client.Promise;
import org.activityinfo.ui.client.component.table.FieldColumn;
import org.activityinfo.ui.client.component.table.InstanceTableView;
import org.activityinfo.ui.client.widget.DisplayWidget;

import java.util.List;
import java.util.Map;

/**
 * Presents the instances of this form class as table
 */
public class TablePresenter implements DisplayWidget<FormTree> {

    private InstanceTableView tableView;

    private FormTree formTree;
    private Map<Cuid, FieldColumn> columnMap;

    private List<FieldColumn> columns;

    public TablePresenter(ResourceLocator resourceLocator) {
        this.tableView = new InstanceTableView(resourceLocator);
    }

    @Override
    public Promise<Void> show(FormTree formTree) {
        this.formTree = formTree;
        enumerateColumns();

        tableView.setCriteria(ClassCriteria.union(formTree.getRootFormClasses().keySet()));
        tableView.setColumns(columns);

        return Promise.nothing();
    }

    @Override
    public Widget asWidget() {
        return tableView.asWidget();
    }

    /**
     *
     * @return a list of possible FieldColumns to display
     */
    private void enumerateColumns() {

        columnMap = Maps.newHashMap();
        columns = Lists.newArrayList();

        enumerateColumns(formTree.getRootFields());
    }

    private void enumerateColumns(List<FormTree.Node> fields) {
        for(FormTree.Node node : fields) {
            if(node.isReference()) {
                enumerateColumns(node.getChildren());
            } else {
                if(columnMap.containsKey(node.getFieldId())) {
                    columnMap.get(node.getFieldId()).addFieldPath(node.getPath());
                } else {
                    FieldColumn col = new FieldColumn(node);
                    columnMap.put(node.getFieldId(), col);
                    columns.add(col);
                }
            }
        }
    }

}
