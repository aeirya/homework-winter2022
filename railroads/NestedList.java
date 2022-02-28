package railroads;

import java.util.ArrayList;
import java.util.List;

public class NestedList<T> {
    private List<List<T>> items;

    public NestedList(int size) {
        items = new ArrayList<>(size);
        for (int i=0; i<size; ++i) {
            items.add(new ArrayList<>());
        }
    }

    public boolean isFree(int item) {
        return items.get(item).isEmpty();
    }

    public List<T> get(int item) {
        return items.get(item);
    }
}
