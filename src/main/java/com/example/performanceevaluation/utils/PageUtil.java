package com.example.performanceevaluation.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 */
public class PageUtil {

    /**
     * 创建分页对象
     *
     * @param current 当前页（从1开始）
     * @param size    每页大小
     * @param <T>     实体类型
     * @return 分页对象
     */
    public static <T> Page<T> createPage(long current, long size) {
        return new Page<>(current, size);
    }

    /**
     * 创建分页对象（带默认值）
     *
     * @param current 当前页
     * @param size    每页大小
     * @param <T>     实体类型
     * @return 分页对象
     */
    public static <T> Page<T> createPage(Integer current, Integer size) {
        long page = current == null || current < 1 ? 1 : current;
        long pageSize = size == null || size < 1 ? 10 : size;
        // 限制最大每页数量
        if (pageSize > 100) {
            pageSize = 100;
        }
        return new Page<>(page, pageSize);
    }

    /**
     * 将IPage转换为自定义分页结果
     *
     * @param page MyBatis-Plus分页对象
     * @param <T>  实体类型
     * @return 分页结果
     */
    public static <T> PageResult<T> toPageResult(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 将List转换为分页结果（内存分页）
     *
     * @param list    数据列表
     * @param current 当前页
     * @param size    每页大小
     * @param <T>     实体类型
     * @return 分页结果
     */
    public static <T> PageResult<T> toPageResult(List<T> list, long current, long size) {
        PageResult<T> result = new PageResult<>();
        long total = list.size();
        long pages = (total + size - 1) / size;
        long start = (current - 1) * size;
        long end = Math.min(start + size, total);

        List<T> records = start < total ? list.subList((int) start, (int) end) : List.of();
        result.setRecords(records);
        result.setTotal(total);
        result.setCurrent(current);
        result.setSize(size);
        result.setPages(pages);
        return result;
    }

    /**
     * 分页结果转换（实体类型转换）
     *
     * @param page    源分页对象
     * @param mapper  转换函数
     * @param <T>     源类型
     * @param <R>     目标类型
     * @return 转换后的分页结果
     */
    public static <T, R> PageResult<R> convert(IPage<T> page, Function<T, R> mapper) {
        PageResult<R> result = new PageResult<>();
        List<R> records = page.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());
        result.setRecords(records);
        result.setTotal(page.getTotal());
        result.setCurrent(page.getCurrent());
        result.setSize(page.getSize());
        result.setPages(page.getPages());
        return result;
    }

    /**
     * 分页结果类
     */
    public static class PageResult<T> {
        /** 数据列表 */
        private List<T> records;
        /** 总记录数 */
        private Long total;
        /** 当前页 */
        private Long current;
        /** 每页大小 */
        private Long size;
        /** 总页数 */
        private Long pages;

        public List<T> getRecords() {
            return records;
        }

        public void setRecords(List<T> records) {
            this.records = records;
        }

        public Long getTotal() {
            return total;
        }

        public void setTotal(Long total) {
            this.total = total;
        }

        public Long getCurrent() {
            return current;
        }

        public void setCurrent(Long current) {
            this.current = current;
        }

        public Long getSize() {
            return size;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public Long getPages() {
            return pages;
        }

        public void setPages(Long pages) {
            this.pages = pages;
        }

        /** 是否有上一页 */
        public boolean hasPrevious() {
            return current != null && current > 1;
        }

        /** 是否有下一页 */
        public boolean hasNext() {
            return current != null && pages != null && current < pages;
        }
    }
}

