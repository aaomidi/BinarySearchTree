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
public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root;

    private List<Node<T>> inorderCache;
    private List<Node<T>> preorderCache;
    private List<Node<T>> postorderCache;

    private transient boolean changesMade = true;

    public BinarySearchTree() {
        root = null;
    }

    public Node<T> getRoot() {
        return root;
    }

    /*public BinarySearchTree(Node root) {
        this.root = root;
    } */

    private int compare(T x, T y) {
        return x.compareTo(y);
    }

    public void add(T data) {
        Node<T> n = new Node<>(data);
        add(root, n);
    }

    private void add(Node<T> start, Node<T> in) {
        changesMade = true;

        if (start == null) {
            this.root = in;
            return;
        }
        int cmp = compare(in.getData(), start.getData());

        if (cmp < 0) {
            if (start.getLeft() == null) {
                start.setLeft(in);
                in.setParent(start);

                balance(start);
            } else {
                add(start.getLeft(), in);
            }
        } else if (cmp > 0) {
            if (start.getRight() == null) {
                start.setRight(in);
                in.setParent(start);

                balance(start);
            } else {
                add(start.getRight(), in);
            }
        }


    }

    public void remove(T data) {
        remove(root, data);
    }

    private void remove(Node<T> start, T data) {
        changesMade = true;

        if (start == null) {
            throw new UnsupportedOperationException("Tree is empty");
        }
        int cmp = compare(data, start.getData());

        if (cmp < 0) {
            remove(start.getLeft(), data);
        } else if (cmp > 0) {
            remove(start.getRight(), data);
        } else {
            removeNode(start);
        }
    }

    private void removeNode(Node<T> n1) {
        Node<T> n2;
        if (n1.getLeft() == null || n1.getRight() == null) {
            if (n1.getParent() == null) {
                this.root = null;
                n1 = null;
                return;
            }
            n2 = n1;
        } else {
            n2 = successor(n1);
            n1.setData(n2.getData());
        }

        Node<T> n3;
        if (n2.getLeft() != null) {
            n3 = n2.getLeft();
        } else {
            n3 = n2.getRight();
        }

        if (n3 != null) {
            n3.setParent(n2.getParent());
        }

        if (n2.getParent() == null) {
            this.root = n3;
        } else {
            if (n2 == n2.getParent().getLeft()) {
                n2.getParent().setLeft(n3);
            } else {
                n2.getParent().setRight(n3);
            }

            balance(n2.getParent());
        }
        n2 = null;
    }

    public void empty() {
        changesMade = true;
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T data) {
        return contains(root, data);
    }

    public boolean contains(Node<T> node, T data) {
        while (node != null) {
            int cmp = compare(data, node.getData());

            if (cmp < 0) {
                node = node.getLeft();
            } else if (cmp > 0) {
                node = node.getRight();
            } else {
                return true;
            }
        }
        return false;
    }

    public List<Node<T>> inorder() {
        if (!changesMade) {
            return inorderCache;
        }

        inorderCache = new ArrayList<>();
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

        postorderCache = new ArrayList<>();
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

        preorderCache = new ArrayList<>();
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

    private void balance(Node<T> node) {
        setBalance(node);
        int balance = node.getBalance();

        if (balance == -2) {
            if (height(node.getLeft().getLeft()) >= height(node.getLeft().getRight())) {
                node = rotateRight(node);
            } else {
                node = doubleRotateLeftRight(node);
            }
        } else if (balance == 2) {
            if (height(node.getRight().getRight()) >= height(node.getRight().getLeft())) {
                node = rotateLeft(node);
            } else {
                node = doubleRotateRightLeft(node);
            }
        }

        if (node.getParent() != null) {
            balance(node.getParent());
        } else {
            this.root = node;
        }
    }

    private void setBalance(Node<T> node) {
        node.setBalance(height(node.getRight()) - height(node.getLeft()));
    }

    private Node<T> successor(Node<T> n1) {
        if (n1.getRight() != null) {
            Node<T> n2 = n1.getRight();
            while ((n2.getLeft() != null)) {
                n2 = n2.getLeft();
            }
            return n2;
        } else {
            Node<T> n2 = n1.getParent();
            while (n2 != null && n1 == n2.getRight()) {
                n1 = n2;
                n2 = n1.getParent();
            }
            return n2;
        }
    }

    private int height(Node<T> node) {
        if (node == null)
            return -1;

        if (node.getLeft() == null && node.getRight() == null) {
            return 0;
        } else if (node.getLeft() == null) {
            return 1 + height(node.getRight());
        } else if (node.getRight() == null) {
            return 1 + height(node.getLeft());
        } else {
            return 1 + max(height(node.getLeft()), height(node.getRight()));
        }
    }

    private Node<T> rotateLeft(Node<T> n1) {
        Node<T> n2 = n1.getRight();
        n2.setParent(n1.getParent());

        n1.setRight(n2.getLeft());

        if (n1.getRight() != null) {
            n1.getRight().setParent(n1);
        }

        n2.setLeft(n1);
        n1.setParent(n2);

        if (n2.getParent() != null) {
            if (n2.getParent().getRight() == n1) {
                n2.getParent().setRight(n2);
            } else if (n2.getParent().getLeft() == n1) {
                n2.getParent().setLeft(n2);
            }
        }
        setBalance(n1);
        setBalance(n2);

        return n2;
    }

    public Node<T> rotateRight(Node<T> n1) {

        Node<T> n2 = n1.getLeft();
        n2.setParent(n1.getParent());

        n1.setLeft(n2.getRight());

        if (n1.getLeft() != null) {
            n1.getLeft().setParent(n1);
        }

        n2.setRight(n1);
        n1.setParent(n2);


        if (n2.getParent() != null) {
            if (n2.getParent().getRight() == n1) {
                n2.getParent().setRight(n2);
            } else if (n2.getParent().getLeft() == n1) {
                n2.getParent().setLeft(n2);
            }
        }

        setBalance(n1);
        setBalance(n2);

        return n2;
    }

    private Node<T> doubleRotateLeftRight(Node<T> n1) {
        n1.setLeft(rotateLeft(n1.getLeft()));
        return rotateRight(n1);
    }

    private Node<T> doubleRotateRightLeft(Node<T> n1) {
        n1.setRight(rotateRight(n1.getRight()));
        return rotateLeft(n1);
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }


    @RequiredArgsConstructor
    public class Node<T extends Comparable<T>> {
        @Getter
        @Setter
        @NonNull
        private T data;
        @Getter
        @Setter
        private Node<T> left;
        @Getter
        @Setter
        private Node<T> right;
        @Getter
        @Setter
        private Node<T> parent;
        @Getter
        @Setter
        private int balance;

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
