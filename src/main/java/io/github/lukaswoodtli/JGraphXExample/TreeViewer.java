package io.github.lukaswoodtli.JGraphXExample;

// mainly from http://stackoverflow.com/questions/22535847/bst-graphical-representation-needed-in-java


import javax.swing.JFrame;


import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;


class MyTree /* implements Comparable<MyTree> */{
	String name;
	MyTree left;
	MyTree right;
	
	public MyTree(String name)
	{
		this.name = name;
	}
	
	public MyTree(String name, MyTree left, MyTree right)
	{
		this.name = name;
		this.left = left;
		this.right = right;
	}

	public int compareTo() {
		return 0;
	}
	
	public String toString() {
		return name;
	}
	
	public MyTree getRight()
	{
		return this.right;
	}
	
	public MyTree getLeft()
	{
		return this.left;
	}

	public int compareTo(MyTree o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}


public class TreeViewer<T> extends JFrame {


static private int CANVAS_HEIGHT = 600;
static private int CANVAS_WIDTH = 1000;

private int rootY = 10;
private int NODE_SIZE = 25;
private int ROW_HEIGHT = 50;
mxGraph graph = new mxGraph();
Object parent = graph.getDefaultParent();



/**
 * draws a tree starting from this root
 * 
 * @param root
 * @param depth
 *            number of nodes to the root (including root)
 * @param index
 *            index of node in this level (leftChildIndex = parentIndex * 2
 *            - 1) and (rightChildIndex = parentIndex * 2)
 * @return
 */
public Object drawTree(MyTree root, int depth, int index) {
    if (root == null) {
        return null;
    }

    // draw root

    /*
     * leftChildIndex = parentIndex * 2 - 1 
     * rightChildIndex = parentIndex *2
     *
     *
     * x = index * canvasWidth / (2^depth + 1)
     *
     * y = depth * canvasHeight / treeDepth
     */

    int myX = (int) ((CANVAS_WIDTH * (index)) / (Math.pow(2, depth - 1) + 1));

    Object rootVertex = graph.insertVertex(parent, null, root,
            myX, depth * ROW_HEIGHT + rootY, NODE_SIZE, NODE_SIZE, "shape=ellipse;fillColor=white;textColor=black;strokeColor=none");



    // recurse for right child

    Object rightChildVertex = drawTree(root.getRight(), depth + 1,
            index * 2);

    if (rightChildVertex != null) {// edge
        graph.insertEdge(parent, null, "", rootVertex, rightChildVertex,
                "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=black");
    }

    Object leftChildVertex = drawTree(root.getLeft(), depth + 1,
            index * 2 - 1);

    // recurse for right child

    if (leftChildVertex != null) { // edge
        graph.insertEdge(parent, null, "", rootVertex, leftChildVertex,
                "startArrow=none;endArrow=none;strokeWidth=1;strokeColor=black");
    }

    return rootVertex;

}

/**
 * Redraw the whole tree
 * 
 * @param root
 *            the root of tree to be drawn
 */

public void update(MyTree root) {

    graph.getModel().beginUpdate();

    try {

        Object[] cells = graph.getChildCells(parent, true, false);
        graph.removeCells(cells, true);
        drawTree(root, 1, 1);

    } finally {
        graph.getModel().endUpdate();
    }
}

public TreeViewer(MyTree root) {

    this.update(root);
    
    mxGraphComponent graphComponent = new mxGraphComponent(graph);

    graphComponent.enableInputMethods(false);
    
    getContentPane().add(graphComponent);
}

public static void main(String[] args) {

	MyTree tree = new MyTree("a",
			new MyTree("b",
					new MyTree("c"), new MyTree("d")),
			new MyTree("e"));

    TreeViewer<MyTree> myTreeViewer=new TreeViewer<MyTree>(tree);
    myTreeViewer.setEnabled(false);
    
    JFrame frame = myTreeViewer;
    
    

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
    frame.setVisible(true);

}



}