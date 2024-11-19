package com.emobile.springtodo.api.request.validation;

public class Patterns {
    public static final String ID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    public static final String COLOUR = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    public static final String STATUS = "TODO|INPROGRESS|COMPLETE|UNCOMPLETED|DELAYED";
}
