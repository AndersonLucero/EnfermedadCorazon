/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

//import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import static jade.core.Runtime.instance;
import java.util.ArrayList;
import weka.core.Attribute;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Attribute;
import weka.core.Debug;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

/**
 *
 * @author Michael
 */
public class Pred {
    
    private String params;

    public Pred(String params) {
        this.params = params;
    }
    
    public void pred(double uno, double dos,double tres, double cuatro, double cinco, double seis, double siete, double ocho, double nueve, double diez, double once, double doce, double trece, int catorce) {

        try {
            
            
            DenseInstance inst = new DenseInstance(14);
            
            ArrayList<Attribute> atts = new ArrayList<>();
            
            atts.add(new Attribute("age",0));
            atts.add(new Attribute("sex",1));
            atts.add(new Attribute("cp",2));
            atts.add(new Attribute("trestbps",3));
            atts.add(new Attribute("chol",4));
            atts.add(new Attribute("fbs",5));
            atts.add(new Attribute("restecg",6));
            atts.add(new Attribute("thalach",7));
            atts.add(new Attribute("exang",8));
            atts.add(new Attribute("oldpeak",9));
            atts.add(new Attribute("slope",10));
            atts.add(new Attribute("ca",11));
            atts.add(new Attribute("thal",12));
            atts.add(new Attribute("class",Arrays.asList("0","1"),13));
            
            System.out.println(atts);
            
            inst.setValue(atts.get(0), uno);
            inst.setValue(atts.get(1), dos);
            inst.setValue(atts.get(2), tres);
            inst.setValue(atts.get(3), cuatro);
            inst.setValue(atts.get(4), cinco);
            inst.setValue(atts.get(5), seis);
            inst.setValue(atts.get(6), siete);
            inst.setValue(atts.get(7), ocho);
            inst.setValue(atts.get(8), nueve);
            inst.setValue(atts.get(9), diez);
            inst.setValue(atts.get(10), once);
            inst.setValue(atts.get(11), doce);
            inst.setValue(atts.get(12), trece);
            inst.setValue(atts.get(13), catorce);

            Instances instances = new Instances("heart", atts, 0);
            
            instances.add(inst);

            System.out.println(instances);
            
            //definir en la instancias donde esta la etiqueta
            instances.setClassIndex(instances.numAttributes() - 1);
           
            //instance.setDataset(instances);
            
            SerializedClassifier serializedClassifier = new SerializedClassifier();
            serializedClassifier.setModelFile(new File("src/archivos/model.train"));
            Classifier mlp = serializedClassifier.getCurrentModel();

            
            Evaluation evaluation = new Evaluation(instances);
            evaluation.evaluateModel(mlp, instances);
            
            

            ArrayList<weka.classifiers.evaluation.Prediction> predictions = evaluation.predictions();
            
           
            
            for (weka.classifiers.evaluation.Prediction prediction : predictions) {
               System.out.println(prediction.predicted());
               if(prediction.predicted() == 0.0){
                   JOptionPane.showMessageDialog(null,prediction.predicted()+" positivo");
               }else{
                    JOptionPane.showMessageDialog(null,prediction.predicted()+" negativo");
               }
            }
            
            
                        
        
        } catch (Exception ex) {
            Logger.getLogger(Pred.class.getName()).log(Level.SEVERE, null, ex);
        }

    }   
}
