package com.aaomidi.bst;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amir on 2015-12-21.
 */
import java.util.LinkedList;
import java.util.List;

public class BinarySearchTree<T extends Comparable<T>> {
    // Size is maintained so creating inorder traversals would be faster.
    private int size = 0;
    private Node<T> root;
    private List<Node<T>> inorderCache;
    private List<Node<T>> preorderCache;
    private List<Node<T>> postorderCache;
    private transient boolean changesMade = true;

    public BinarySearchTree() {
        root = null;
    }


    public int getSize() {
        return size;
    }

    public Node<T> getRoot() {
        return root;
    }

    private int compare(T data1, T data2) {
        return data1.compareTo(data2);
    }

    public List<Node<T>> inorder() {
        if (!changesMade) {
            return inorderCache;
        }

        inorderCache = new LinkedList<>();
        inorder(root, inorderCache);
        return inorderCache;
    }

    private void inorder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        inorder(node.getLeft(), list);
        list.add(node);
        inorder(node.getRight(), list);
    }

    public List<Node<T>> postorder() {
        if (!changesMade) {
            return postorderCache;
        }

        postorderCache = new LinkedList<>();
        postorder(root, postorderCache);
        return postorderCache;
    }

    private void postorder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        postorder(node.getLeft(), list);
        postorder(node.getRight(), list);
        list.add(node);
    }

    public List<Node<T>> preorder() {
        if (!changesMade) {
            return preorderCache;
        }

        preorderCache = new LinkedList<>();
        preorder(root, preorderCache);
        return preorderCache;
    }

    private void preorder(Node<T> node, List<Node<T>> list) {
        if (node == null) {
            return;
        }
        list.add(node);
        preorder(node.getLeft(), list);
        preorder(node.getRight(), list);
    }

    private int height(Node<T> node) {
        return node == null ? -1 : node.getHeight();
    }

    private int max(int left, int right) {
        return left > right ? left : right;
    }

    private Node<T> rotateWithLeftChild(Node<T> n2) {
        Node<T> n1 = n2.left;

        n2.left = n1.right;
        n1.right = n2;

        n2.height = max(height(n2.left), height(n2.right)) + 1;
        n1.height = max(height(n1.left), n2.height) + 1;

        return n1;
    }

    private Node<T> rotateWithRightChild(Node<T> n1) {
        Node<T> n2 = n1.right;
        n1.right = n2.left;
        n2.left = n1;

        n1.height = max(height(n1.left), height(n1.right)) + 1;
        n2.height = max(height(n2.right), n1.height) + 1;
        return n2;
    }

    private Node<T> doubleRotateWithLeftChild(Node<T> n1) {
        n1.left = rotateWithRightChild(n1.left);
        return rotateWithLeftChild(n1);
    }

    private Node<T> doubleRotateWithRightChild(Node<T> n1) {
        n1.right = rotateWithLeftChild(n1.right);
        return rotateWithRightChild(n1);
    }

    private Node<T> findMax(Node<T> node) {
        if (node == null) {
            return null;
        }

        while (node.right != null) {
            node = node.right;
        }

        return node;
    }

    private Node<T> findMin(Node<T> node) {
        if (node == null) {
            return null;
        }

        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    public void insert(T data) {
        size++;
        root = insert(data, root);
    }

    private Node<T> insert(T data, Node<T> node) {
        if (node == null) {
            node = new Node<>(data);
        } else {

            int cmp = compare(data, node.data);

            if (cmp < 0) {

                node.left = insert(data, node.left);

                if (height(node.left) - height(node.right) == 2) {
                    if (compare(data, node.left.data) < 0) {
                        node = rotateWithLeftChild(node);
                    } else {
                        node = doubleRotateWithLeftChild(node);
                    }
                }

            } else if (cmp > 0) {

                node.right = insert(data, node.right);

                if (height(node.right) - height(node.left) == 2) {
                    if (compare(data, node.right.data) > 0) {
                        node = rotateWithRightChild(node);
                    } else {
                        node = doubleRotateWithRightChild(node);
                    }
                }

            } else {
                size--;
            }

        }
        node.height = max(height(node.left), height(node.right)) + 1;
        return node;
    }

    public void remove(T data) {
        size--;
        root = remove(data, root);
    }

    public Node<T> remove(T x, Node<T> n) {
        if (n == null) {
            size++;
            return null;
        }
        int cmp = compare(x, n.data);

        if (cmp < 0) {

            n.left = remove(x, n.left);
            n.height = max(height(n.left), height(n.right)) + 1;
            return n;

        } else if (cmp > 0) {

            n.right = remove(x, n.right);
            n.height = max(height(n.left), height(n.right)) + 1;
            return n;

        } else {

            if (n.left == null && n.right == null) {
                return null;
            }

            if (n.left == null) {
                return n.right;

            } else if (n.right == null) {
                return n.left;
            }

            T tmp = findMin(n.getRight()).data;
            n.data = tmp;

            n.setRight(remove(n.data, n.right));
            n.height = max(height(n.left), height(n.right)) + 1;
            return n;

        }
    }

    public boolean contains(T data) {
        return find(data) != null;
    }

    public Node<T> find(T data) {
        return find(data, root);
    }

    private Node<T> find(T data, Node<T> node) {
        while (node != null) {
            int cmp = compare(data, node.data);
            if (cmp < 0) {
                node = node.left;
            } else if (cmp > 0) {
                node = node.right;
            } else {
                return node;
            }
        }
        return null;
    }

    public class Node<Data extends Comparable<Data>> {
        private Data data;
        private Node<Data> left;
        private Node<Data> right;
        private int height = 0;

        public Node(Data data) {
            this.data = data;
        }

        public Node(Data data, Node<Data> left, Node<Data> right) {
            this.data = data;

            this.left = left;
            this.right = right;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Node<Data> getLeft() {
            return left;
        }

        public void setLeft(Node<Data> left) {
            this.left = left;
        }

        public Node<Data> getRight() {
            return right;
        }

        public void setRight(Node<Data> right) {
            this.right = right;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node)) return false;

            Node<?> node = (Node<?>) o;

            return data.equals(node.data);

        }

        @Override
        public int hashCode() {
            return data.hashCode();
        }
    }
}
