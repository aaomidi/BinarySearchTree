import com.aaomidi.bst.BinarySearchTree;
import org.junit.Test;

/**
 * Created by amir on 2015-12-21.
 */
public class BSTTester {
    @Test
    public void testBinarySearchTree() {
        BinarySearchTree<String> bst = new BinarySearchTree<>();

        bst.add("Java");
        bst.add("Programming");
        bst.add("C");
        bst.add("Assembly");
        bst.add("Architecture");
        bst.add("Single Cycle");
        bst.add("Multi Cycle");
        bst.add("Pipelines");
        bst.add("Trees");
        bst.add("Graphs");
        bst.add("Binary");
        bst.add("Hex");

        for (BinarySearchTree.Node nodes : bst.inorder()) {
            String data = (String) nodes.getData();
            System.out.println(data);
        }
    }
}
