import com.aaomidi.bst.BinarySearchTree;
import org.junit.Test;

/**
 * Created by amir on 2015-12-21.
 */
public class BSTTester {
    @Test
    public void testBinarySearchTree() {
        BinarySearchTree<String> bst = new BinarySearchTree<>();

        bst.insert("Java");
        bst.insert("Programming");
        bst.insert("C");
        bst.insert("Assembly");
        bst.insert("Architecture");
        bst.insert("Single Cycle");
        bst.insert("Multi Cycle");
        bst.insert("Pipelines");
        bst.insert("Trees");
        bst.insert("Graphs");
        bst.insert("Binary");
        bst.insert("Hex");

        for (BinarySearchTree.Node nodes : bst.inorder()) {
            String data = (String) nodes.getData();
            System.out.println(data);
        }
    }
}
