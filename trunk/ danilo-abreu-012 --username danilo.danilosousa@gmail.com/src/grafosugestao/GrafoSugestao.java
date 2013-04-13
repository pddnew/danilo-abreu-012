/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package grafosugestao;

import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.*;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author Leticia
 */
public class GrafoSugestao {
    
    static String message;
    static String message1;
    
    
    public static void main(String[] args) {
        
        final Graph<Vertice, String> g = new SparseMultigraph<Vertice, String>();
        
        g.addEdge("A", new Cliente("Ana"), new Produto("lápis", "papelaria", 1));
        g.addEdge("B", new Cliente("Ana"), new Produto("borracha", "papelaria", 1.50));
        g.addEdge("C", new Cliente("Ana"), new Produto("caderno", "papelaria", 5));
        
        g.addEdge("D", new Cliente("Pedro"), new Produto("caderno", "papelaria", 5));
        g.addEdge("E", new Cliente("Pedro"), new Produto("caneta", "papelaria", 2));
        
        g.addEdge("F", new Cliente("Carlos"), new Produto("caneta", "papelaria", 2));
        //g.addEdge("G", new Cliente("Carlos"), new Produto("estojo", "papelaria", 3.75));
        
        g.addEdge("H", new Cliente("João"), new Produto("lápis", "papelaria", 1));
        
        g.addEdge("I", new Cliente("Maria"), new Produto("lápis", "papelaria", 1));
        g.addEdge("J", new Cliente("Maria"), new Produto("borracha", "papelaria", 1.50));
        g.addEdge("K", new Cliente("Maria"), new Produto("corretivo", "papelaria", 4));
        
        g.addEdge("L", new Cliente("Patrícia"), new Produto("estojo", "papelaria", 3.75));
        g.addEdge("M", new Cliente("Patrícia"), new Produto("marca_texto", "papelaria", 3.69));
        g.addEdge("N", new Cliente("Patrícia"), new Produto("lapiseira", "papelaria", 10.59));
        
        g.addEdge("O", new Cliente("Denis"), new Produto("estojo", "papelaria", 3.75));
        g.addEdge("P", new Cliente("Denis"), new Produto("lapiseira", "papelaria", 10.59));
        
        g.addEdge("Q", new Cliente("Carla"), new Produto("mouse", "informática", 30));
        g.addEdge("R", new Cliente("Carla"), new Produto("mouse_pad", "informática", 10.75));
        
        g.addEdge("S", new Cliente("Luiz"), new Produto("notebook", "informática", 3000));
        g.addEdge("T", new Cliente("Luiz"), new Produto("mouse", "informática", 30));
        
        g.addEdge("U", new Cliente("Manuella"), new Produto("computador", "informática", 3599.99));
        
        
        
        
        //modificação das cores dso vértices
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
        
        //definindo designe da tela de apresentação/execução
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

        final JComboBox cboClients = new JComboBox(getListAllClient(g).toArray());          
        final JComboBox cboDepths = new JComboBox(getListAllDepths().toArray()); 
        final JLabel msg1 = new JLabel("Digite o nome do cliente: ");
        final JLabel msg2 = new JLabel("Digite a que distância será feita a busca para sugestão: ");
         
        
        button.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                 // Lógica 
                Collection<Vertice> collection = g.getNeighbors(new Cliente(cboClients.getSelectedItem().toString()));
                // Produtos adquiridos
                ArrayList<Vertice> produtosAdquiridos = new ArrayList(collection);        
                // Produtos Sugeridos
                ArrayList<Vertice> ls = getSuggestLevel(g, new Cliente(cboClients.getSelectedItem().toString()), 
                        Integer.parseInt(cboDepths.getSelectedItem().toString()));
                
                ls = getSuggest0(produtosAdquiridos, ls);

                //Mensagem para apresentar na janela de resposta
                message = ls.toString();
                
                if( ls.isEmpty()){
                    Collection<Vertice> coll = g.getVertices();  
                    ArrayList<Vertice> ls1 = new ArrayList<Vertice>();
                    for(Vertice v : new ArrayList<Vertice>(coll)){
                        if(v instanceof Produto)
                                if(((Produto)v).getCategoria().equalsIgnoreCase(((Produto)produtosAdquiridos.get(0)).getCategoria()))
                                    ls1.add(v);
                    }
                    message1 = ls1.toString();
                    JOptionPane.showMessageDialog(null, "Cliente: " + cboClients.getSelectedItem().toString()+ ":\n\n- Produtos adquiridos:\n" +produtosAdquiridos+ "\n\n- produtos Sugeridos:\n" +message1);
                }
                else         
                    JOptionPane.showMessageDialog(null, "Cliente: " + cboClients.getSelectedItem().toString()+ ":\n\n- Produtos adquiridos:\n" +produtosAdquiridos+ "\n\n- produtos Sugeridos:\n" +message);
            }
        });
        
        vv.add(msg1);
        vv.add(cboClients);
        vv.add(msg2);
        vv.add(cboDepths);
        vv.add(button);
        
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);        
    }
    
    //método que retorna um array com todos os produtos da busca, até determinada distância
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
    
    //método que seleciona somente os produtos que serão sugeridos, retirando os produtos já adquiridos pelo cliente
    public static ArrayList<Vertice> getSuggest0(ArrayList<Vertice> adquiridos, ArrayList<Vertice> sugeridos){
        
        for(Vertice v: adquiridos)
            if(sugeridos.contains(v))
                sugeridos.remove(v);
        
        return sugeridos;
    }
    

     //método que retorna um array com todos os clientes do grafo para preencher o combobox
     public static ArrayList<Vertice> getListAllClient(Graph<Vertice, String> g){
    
        Collection<Vertice> coll1 = g.getVertices();
        
        ArrayList<Vertice> ls2 = new ArrayList<Vertice>();
        
        for(Vertice v : new ArrayList<Vertice>(coll1))
            if(v instanceof Cliente)
                ls2.add(v);
        
        return ls2;
    }
    
     //método que retorna um array com os valores da distância para preencher o combobox
    public static ArrayList<Integer> getListAllDepths(){
        
        ArrayList<Integer> depths = new ArrayList<Integer>();
                
        for(int i = 4; i < 7; i++)
            depths.add(i);
        
        return depths;
    }
}

