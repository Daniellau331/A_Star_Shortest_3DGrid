import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;

public class MtStHelensDiv_917307744 implements AIModule{
    public class NodeComparator implements Comparator<Node>{

        public int compare(MtStHelensDiv_917307744.Node node_1, MtStHelensDiv_917307744.Node node_2) {
            double node_1_f_val = node_1.g_val + node_1.h_val;
            double node_2_f_val = node_2.g_val + node_2.h_val;
            if(node_1_f_val==node_2_f_val){
                return 0;
            }else if(node_1_f_val>node_2_f_val){
                return 1;
            }else{
                return -1;
            }
        }
    }
    
    public class Node {
        private Node  nodeParent;
        private Point point;
        private double g_val;
        private double h_val;
    
        public Node(Node nodeParent, Point point, double g_val , double h_val){
            this.nodeParent =nodeParent;
            this.point = point;
            this.g_val = g_val;
            this.h_val = h_val;
        }
    }

    // traceback the path 
    // improved, not using stack, only using one for-loop now
    public List<Point> getPath(Node node){
        ArrayList<Point> path = new ArrayList<Point>();
        for(Node current = node; current!=null ; current = current.nodeParent){
            path.add(0,current.point);
        }
        return path;
    }

    // heuristic funciton for Div cost function
    private double getHeuristic(final TerrainMap map, final Point startingPoint) {
        
        Point endPoint = map.getEndPoint();
        double chebyshev_distance = Math.max(Math.abs(endPoint.x - startingPoint.x), Math.abs(endPoint.y - startingPoint.y));
        return chebyshev_distance*(0.5);
    }

    public List<Point> createPath(TerrainMap map) {

        HashMap<Point, Node> closed = new HashMap<Point, Node>(366600);
        Comparator<Node> comparator = new NodeComparator();
        PriorityQueue<Node> open = new PriorityQueue<Node>(366600, comparator);

        Node Starting_node = new Node(null,map.getStartPoint(),0.0,getHeuristic(map, map.getStartPoint()));
        open.add(Starting_node);
        while(!open.isEmpty()){
            // expand node with min cost path
            Node currentPoint = open.poll();

            //check if we have reached the goal 
            if(currentPoint.point.equals(map.getEndPoint())){
                return getPath(currentPoint);
            }
            
            //iterate all neighbours of the currentPoint
            Point[] neighbors = map.getNeighbors(currentPoint.point);
            for(Point neighbor : neighbors){
                // never discover this neighbor --> the neighbor does not have a g value
                if(closed.get(neighbor)==null){
                    Node newNode = new Node(currentPoint,neighbor, currentPoint.g_val + map.getCost(currentPoint.point, neighbor),getHeuristic(map, neighbor));
                    open.add(newNode);
                    closed.put(neighbor, newNode);
                }else{
                    // neighbor got discovered before, the neighbor has a g value (cost to get to neighbor)
                    Node neighbor_node = closed.get(neighbor);
                    if(neighbor_node.g_val>currentPoint.g_val+map.getCost(currentPoint.point, neighbor)){
                        open.remove(neighbor_node);
                        closed.remove(neighbor_node.point);
                        Node newNode = new Node(currentPoint,neighbor,currentPoint.g_val+map.getCost(currentPoint.point, neighbor),getHeuristic(map, neighbor));
                        open.add(newNode);
                        closed.put(neighbor, newNode);
                    }
                }
            }
        }

        return null;
    }
}
