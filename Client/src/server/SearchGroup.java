package server;

import server.structs.DataClass;

import java.util.ArrayList;
import java.util.Map;

public class SearchGroup {
    public static Map<String,DataClass> getFilteredGroup() {
        return filteredGroup;
    }

    public static void setFilteredGroup(Map<String,DataClass> filteredGroup) {
        SearchGroup.filteredGroup = filteredGroup;
    }

    public static Map<String,DataClass> filteredGroup;
}