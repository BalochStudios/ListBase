package com.example.ListBase;

public class ToDoList {
    private int listId;
    public String listName;

    public ToDoList() {
    }

    public ToDoList(int listId, String listName) {
        this.listId = listId;
        this.listName = listName;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
