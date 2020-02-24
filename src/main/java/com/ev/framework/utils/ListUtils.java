package com.ev.framework.utils;

import lombok.Cleanup;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AirOrangeWorkSpace
 *
 */
public class ListUtils extends org.apache.commons.collections.ListUtils{
	public static <T> LinkedList<T> filter(Collection<T> collection, Filter<T> filter) {
        LinkedList<T> filtered = new LinkedList<>();
        for (T t : collection) {
            if (!filter.isOut(t)) {
                filtered.add(t);
            }
        }
        return filtered;
    }
    
    public interface Filter<T> {
        boolean isOut(T object);
    }

    public static <E> List<E> getDuplicateElements(List<E> list) {
        return list.stream() // list 对应的 Stream
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .entrySet().stream() // 所有 entry 对应的 Stream
                .filter(entry -> entry.getValue() > 1) // 过滤出元素出现次数大于 1 的 entry
                .map(Map.Entry::getKey) // 获得 entry 的键（重复元素）对应的 Stream
                .collect(Collectors.toList());  // 转化为 List
    }

    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        @Cleanup ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }


}
