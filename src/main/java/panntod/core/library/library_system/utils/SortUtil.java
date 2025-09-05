package panntod.core.library.library_system.utils;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SortUtil {
    private SortUtil() {
        // private constructor supaya tidak bisa diinstansiasi
    }

    public static Sort parseSortParam(List<String> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return Sort.by(Sort.Order.desc("updatedAt"));
        }

        if (sortList.size() == 2) {
            String property = sortList.get(0).trim();
            String directionStr = sortList.get(1).trim();
            Sort.Direction direction = directionStr.equalsIgnoreCase("asc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            return Sort.by(new Sort.Order(direction, property));
        }

        return Sort.by(sortList.stream().map(s -> {
            String[] parts = s.split(",");
            String property = parts[0].trim();
            Sort.Direction direction = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            return new Sort.Order(direction, property);
        }).toList());
    }

}
