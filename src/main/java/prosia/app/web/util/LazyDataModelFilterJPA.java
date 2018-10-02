/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prosia.app.web.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Ismail
 */
public abstract class LazyDataModelFilterJPA<T> extends LazyDataModel<T> {

    private JpaRepository<T, Serializable> repository;

    public LazyDataModelFilterJPA(JpaRepository<T, Serializable> repository) {
        this.repository = repository;
    }

    @Override
    public T getRowData(String rowKey) {
        try {
            // Integer ID
            return repository.findOne(Integer.parseInt(rowKey));
        } catch (NumberFormatException e) {
            // String ID
            return repository.findOne(rowKey);
        }
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        this.setRowCount((int) getDataSize(filters));
        this.setPageSize(pageSize);
        if (sortOrder == SortOrder.UNSORTED || sortField == null || sortField.isEmpty()) {
            PageRequest request = new PageRequest(first / pageSize, pageSize);
            return getDatas(request, filters).getContent();
        } else {
            Sort sort = new Sort(sortOrder == SortOrder.ASCENDING
                    ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
            PageRequest request = new PageRequest(first / pageSize, pageSize, sort);
            return getDatas(request, filters).getContent();
        }

    }

    /**
     * Override this method to change the behaviors of load data by filter. For
     * example, to load data with search parameters.
     *
     * @param request pageRequest contains pagination and sorting default from
     * primefaces datatable.
     * @param filters contains filters object key and value
     * @return
     */
    protected abstract Page<T> getDatas(PageRequest request, Map<String, Object> filters);

    /**
     * Override this method to change the behaviors of count the data by filter.
     * Basically, this method must be overrides if the method getDatas() is also
     * overrides.
     *
     * @param filters contains filters object key and value
     * @return
     */
    protected abstract long getDataSize(Map<String, Object> filters);

}