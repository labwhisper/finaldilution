package com.labessence.biotech.finaldilution.peripherals;

import com.labessence.biotech.finaldilution.genericitem.Item;

import java.util.List;

/**
 * Project: FinalDilution
 * Created by dawid.chmielewski on 9/10/2017.
 */

public interface DataGatewayOperations<T extends Item> {
    void save(T item);

    void update(T item);

    void remove(T item);

    T load(String name);

    List<T> loadAll();
}
