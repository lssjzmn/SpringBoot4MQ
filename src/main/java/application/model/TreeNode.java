package application.model;

import java.util.*;

/**
 * User: lancec
 * Date: 12-8-14
 * Defines a simple tree structure.
 */
public abstract class TreeNode<T extends TreeNode> implements java.io.Serializable, Cloneable {
    /**
     * The unique ID.
     */
    protected String id;
    /**
     * The parent ID.
     */
    protected String parentId;
    /**
     * The parent element.
     */
    protected T parent;
    /**
     * The children list.
     */
    protected List<T> children = new ArrayList<T>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public T getRoot() {
        T current = (T) this;
        while (!current.isRootNode()) {
            current = (T) current.getParent();
        }
        return current;
    }

    public List<T> getChildren() {
        return children;
    }

    /**
     * Add a child to this element.
     *
     * @param child The child element
     */
    public void addChild(T child) {
        child.setParentId(this.id);
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Add a collection elements as current children.
     *
     * @param children The collection of children
     */
    public void addChildren(Collection<? extends T> children) {
        for (T child : children) {
            addChild(child);
        }
    }

    /**
     * Determine whether this element is the root.
     *
     * @return Return true if it's root
     */
    public boolean isRootNode() {
        return this.parent == null;
    }

    /**
     * Determine whether this element is the root, and has not children.
     *
     * @return Return true if it's root  and has no children
     */
    public boolean isRootOnly() {
        return isRootNode() && isLeaf();
    }

    /**
     * Determine whether this element is a leaf element.
     *
     * @return Return true if it's leaf
     */
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    /**
     * Find the Node by id from current node.
     *
     * @param id The find id
     * @return Return null if not found
     */
    public T find(String id) {
        Queue<T> queue = new LinkedList<T>();
        T root = (T) this;
        queue.add(root);
        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (id.equals(current.getId())) {
                return current;
            }
            queue.addAll(current.getChildren());
        }
        return null;
    }

    /**
     * Find the Node by id from current node.
     *
     * @param pathIds The parent IDs
     * @param id      The find id
     * @return Return null if not found
     */
    public T find(List<String> pathIds, String id) {
        Queue<T> queue = new LinkedList<T>();
        T root = (T) this;
        queue.add(root);
        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (pathIds == null || pathIds.isEmpty()) {
                if (current.getParentId() == null) {
                    if (id.equals(current.getId())) {
                        return current;
                    }
                }
            } else {
                List<String> parents = getParentIDs(current);
                boolean parentEquals = true;
                if (parents.size() == pathIds.size()) {
                    for (int i = 0; i < parents.size(); i++) {
                        if (!parents.get(i).equals(pathIds.get(i))) {
                            parentEquals = false;
                            break;
                        }
                    }
                } else {
                    parentEquals = false;
                }
                if (parentEquals && current.getId().equals(id)) {
                    return current;
                }
            }
            queue.addAll(current.getChildren());
        }
        return null;
    }

    /**
     * Get the index of a specify child element.
     *
     * @param child The child element
     * @return Return -1 if not found
     */
    public int getChildIndex(TreeNode child) {
        if (child == null) {
            return -1;
        }
        return this.getChildren().indexOf(child);
    }

    /**
     * Get leaves under current node.
     *
     * @return Return the leaves list
     */
    public List<T> getLeaves() {
        List<T> leaves = new ArrayList<T>();
        Queue<T> queue = new LinkedList<T>();
        queue.add((T) this);
        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (current.isLeaf()) {
                leaves.add(current);
            }
            queue.addAll(current.getChildren());
        }
        return leaves;
    }

    /**
     * Traversal tree from this tree node by level.
     *
     * @return Return the traversal result list
     */
    public List<T> traversal() {
        List<T> result = new ArrayList<T>();
        Queue<T> queue = new LinkedList<T>();
        queue.add((T) this);
        while (!queue.isEmpty()) {
            T current = queue.poll();
            result.add(current);
            queue.addAll(current.getChildren());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeNode)) return false;

        TreeNode that = (TreeNode) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
     * Make a Tree from a list, the items of list must be the subclass of TreeStructure, id and parentId is required.
     *
     * @param list The list of sources
     * @param <T>  the items of list must be the subclass of TreeStructure
     * @return Return the root list of this tree
     */
    public static <T extends TreeNode> List<T> makeTreeFromList(List<? extends T> list) {
        List<T> roots = new ArrayList<T>();
        for (T item : list) {
            if (item.getId() == null) {
                continue;
            }
            for (T item1 : list) {
                if (item.getId().equals(item1.getParentId())) {
                    item.addChild(item1);
                }
                if (item.getParentId() != null && item.getParentId().equals(item1.getId())) {
                    item.setParent(item1);
                }
            }
            if (item.getParentId() == null) {
                roots.add(item);
            }
        }
        return roots;
    }

    /**
     * Get the parents by current id and the source list, the items of list must be the subclass of TreeStructure, id and parentId is required.
     *
     * @param id              The current id
     * @param list            The list of sources
     * @param containChildren If true, each parent contains the children
     * @param <T>             the items of list must be the subclass of TreeStructure
     * @return Return parents of current id, the first item is the Root, the last one is current. If current item not found in the list, then return empty list.
     */
    public static <T extends TreeNode> List<T> getParents(String id, List<? extends T> list, boolean containChildren) {
        List<T> parents = new ArrayList<T>();
        T current = null;
        for (T item : list) {
            if (id.equals(item.getId())) {
                current = item;
                break;
            }
        }
        if (current == null) {
            return parents;
        }
        Queue<T> queue = new LinkedList<T>();
        queue.add(current);
        while (!queue.isEmpty()) {
            T item = queue.poll();
            if (containChildren) {
                item.addChildren(getChildren(item.getId(), list));
            }
            parents.add(0, item);
            if (item.getParentId() == null) {
                break;
            }
            for (T t : list) {
                if (item.getParentId().equals(t.getId())) {
                    queue.add(t);
                    break;
                }
            }
        }
        return parents;
    }

    /**
     * Get the children by current id and the source list, the items of list must be the subclass of TreeStructure, id and parentId is required.
     *
     * @param id   The current id
     * @param list The list of sources
     * @param <T>  the items of list must be the subclass of TreeStructure
     * @return Return children list of current id.
     */
    public static <T extends TreeNode> List<T> getChildren(String id, List<? extends T> list) {
        List<T> children = new ArrayList<T>();
        T current = null;
        for (T item : list) {
            if (id.equals(item.getId())) {
                current = item;
                break;
            }
        }
        if (current == null) {
            return children;
        }
        for (T t : list) {
            if (current.getId().equals(t.getParentId())) {
                children.add(t);
            }
        }
        return children;
    }

    /**
     * Get the Roots of the tree list, the items of list must be the subclass of TreeStructure, id and parentId is required.
     *
     * @param list The list of sources
     * @param <T>  the items of list must be the subclass of TreeStructure
     * @return Return Roots list.
     */
    public static <T extends TreeNode> List<T> getRoots(List<? extends T> list) {
        List<T> roots = new ArrayList<T>();
        for (T t : list) {
            if (t.getParentId() == null) {
                roots.add(t);
            }
        }
        return roots;
    }

    /**
     * Get the parents ID list of current.
     * The first element is current parent, the last element is the root.
     *
     * @return Return the list of parents ID
     */
    public static List<String> getParentIDs(TreeNode current) {
        List<String> parents = new ArrayList<String>();
        while (current.getParent() != null) {
            TreeNode parent = current.getParent();
            parents.add(parent.getId());
            current = parent;
        }
        return parents;
    }

    /**
     * Get the parents list of current.
     * The first element is current parent, the last element is the root
     *
     * @param current The current element
     * @return Return the list of parents
     */
    public static List<TreeNode> getParents(TreeNode current) {
        List<TreeNode> parents = new ArrayList<TreeNode>();
        while (current.getParent() != null) {
            TreeNode parent = current.getParent();
            parents.add(parent);
            current = parent;
        }
        return parents;
    }
}
