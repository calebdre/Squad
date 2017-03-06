package com.squad.view.helpers.ui_items;

import com.squad.model.Category;

public class CategoryUiItem {
    private Category category;

    public CategoryUiItem(Category category) {
        this.category = category;
    }

    public String name() {
        return category.name();
    }
}
