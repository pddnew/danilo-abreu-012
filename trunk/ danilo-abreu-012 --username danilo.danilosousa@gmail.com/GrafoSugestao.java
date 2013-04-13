/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafosugestao;

import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Leticia
 */
public class GrafoSugestao {

    private static ArrayList<Vertice> visited = new ArrayList<Vertice>();
    private static ArrayList<Vertice> suggest = new ArrayList<Vertice>();
    private static ArrayList<Vertice> adjacentes = new ArrayList<Vertice>();
    
    static String message;
    
    
    public static void main(String[] args) {
        
        Graph<Vertice, String> g = new DirectedSparseGraph<Vertice, String>(); 
        
        g.addEdge("A", new Cliente("Ana"), new Produto("lápis", 1));
        g.addEdge("B", new Cliente("Ana"), new Produto("borracha", 1.5));
        g.addEdge("C", new Cliente("Ana"), new Produto("caderno", 5));
        
        g.addEdge("D", new Cliente("Pedro"), new Produto("caderno", 5));
        g.addEdge("E", new Cliente("Pedro"), new Produto("caneta", 2));
        
        g.addEdge("F", new Cliente("Carlos"), new Produto("caneta", 2));
        g.addEdge("G", new Cliente("Carlos"), new Produto("estojo", 3.75));
        
        g.addEdge("H", new Cliente("João"), new Produto("lápis", 1));
        
        g.addEdge("I", new Cliente("Maria"), new Produto("lápis", 1));
        g.addEdge("J", new Cliente("Maria"), new Produto("borracha", 1.5));
        g.addEdge("K", new Cliente("Maria"), new Produto("corretivo", 4));
        
        g.addEdge("L", new Cliente("Patrícia"), new Produto("estojo", 3.75));
        g.addEdge("M", new Cliente("Patrícia"), new Produto("marca_texto", 3.69));
        g.addEdge("N", new Cliente("Patrícia"), new Produto("lapiseira", 10.59));
        
        g.addEdge("O", new Cliente("Denis"), new Produto("estojo", 3.75));
        g.addEdge("P", new Cliente("Denis"), new Produto("lapiseira", 10.59));
        
        
        
        // Lógica 
        Collection<Vertice> collection = g.getNeighbors(new Cliente("Ana"));
        // Produtos adquiridos
        ArrayList<Vertice> produtosAdquiridos = new ArrayList(collection);        
        // Produtos Sugeridos
        ArrayList<Vertice> ls = getSuggestLevel(g, new Cliente("Ana"), 3);
        
        System.out.println("Produtos adquiridos : \n" + produtosAdquiridos);
        ls = getSuggest0(produtosAdquiridos, ls);
        System.out.println("Produtos sugeridos : \n" + ls);
        
        //Mensagem para apresentar na MessagePane
        message = ls.toString();
        
        
        
        
        // View stuff. 
        final Transformer<Vertice, Paint> verticeCor  = new Transformer<Vertice, Paint>(){
             @Override
             public Paint transform(Vertice arg0){                 
                 if(arg0 instanceof Cliente) {
                     return Color.ORANGE;
                 }else {
                     return Color.BLACK;
                 }
             }
        };
        
        Layout<Vertice, String> l = new ISOMLayout<Vertice, String>(g);
        l.setSize(new Dimension(1280, 680));
        VisualizationViewer<Vertice, String> vv = new VisualizationViewer<Vertice, String>(l);        
        vv.getRenderContext().setVertexFillPaintTransformer(verticeCor);        
        // Rótulo do Vertice
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Vertice>());        
        // Rótulo da Aresta
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<String>());
        
        // Tela
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Busca");
        
        final JTextField text0 = new JTextField("Digite o Cliente");
        JTextField text1 = new JTextField("Digite a profundidade");
                
        button.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                 // Lógica 
                Collection<Vertice> collection = g.getNeighbors(new Cliente(text0.getText()));
                // Produtos adquiridos
                ArrayList<Vertice> produtosAdquiridos = new ArrayList(collection);        
                // Produtos Sugeridos
                ArrayList<Vertice> ls = getSuggestLevel(g, new Cliente(text0), text1);

                //System.out.println("Produtos adquiridos : \n" + produtosAdquiridos);
                ls = getSuggest0(produtosAdquiridos, ls);
                //System.out.println("Produtos sugeridos : \n" + ls);

                //Mensagem para apresentar na MessagePane
                message = ls.toString();
                // Acao Calcular                
                JOptionPane.showMessageDialog(null, text0.getText()+ ":\n\n- Produtos adquiridos:\n" +produtosAdquiridos+ "\n\n- produtos Sugeridos:\n" +message);
            }
        });
        
        vv.add(text0);
        vv.add(text1);
        vv.add(button);
        
        frame.getContentPane().add(vv);
        //getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);        
    }
    
    public static ArrayList<Vertice> getSuggestLevel(Graph<Vertice, String> g, Vertice vNode, int level){
        
        KNeighborhoodFilter<Vertice, String> kNeigh = 
                new KNeighborhoodFilter<Vertice, String>(vNode, level, KNeighborhoodFilter.EdgeType.IN_OUT);
        
        Graph<Vertice, String> g2 =  kNeigh.transform(g);
                
        ArrayList<Vertice> suggest1 = new ArrayList<Vertice>();
        
        for(Vertice v : new ArrayList<Vertice>(g2.getVertices())){
            if(v instanceof Produto){
                suggest1.add(v);
            }
        }
        
        return suggest1;
    }
    
    public static ArrayList<Vertice> getSuggest0(ArrayList<Vertice> adquiridos, ArrayList<Vertice> sugeridos){
        
        for(Vertice v: adquiridos)
            if(sugeridos.contains(v))
                sugeridos.remove(v);
        
        return sugeridos;
    }
    
//    public static ArrayList<Vertice> getSuggest(Graph<Vertice, String> g, Vertice vNode, int level){    
//        
//        adjacentes.addAll( new ArrayList<Vertice>(g.getNeighbors(vNode)));
//        
//        System.out.println(level);
//        
//        if(dist >= level ){ 
//            return suggest;
//        }
//        
//        visited.add(vNode);
//        
//        for(int i = 0; i < adjacentes.size(); i++){
//            Vertice v1 = adjacentes.get(i);
//            
//            if(!visited.contains(v1)){
//                dist++;
//                if(v1 instanceof Produto && !suggest.contains(v1)){                    
//                    suggest.add(v1);
//                }
//                return getSuggest(g, v1, level);
//            }
//            if(i+1 == adjacentes.size()){
//                dist--;
//            }
//        }
//       
//        return suggest;
//    }
        
}

