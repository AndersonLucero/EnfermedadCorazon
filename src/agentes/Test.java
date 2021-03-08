/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Instances;

/**
 *
 * @author USRBET
 */
public class Test extends Agent {

    @Override
    protected void setup() {
        addBehaviour(new Comportamiento());
    }

    @Override
    protected void takeDown() {
        System.out.println("");
    }

    class Comportamiento extends Behaviour {

        @Override
        public void action() {
            System.out.println("Soy: " + getName());
            //doDelete();//matar al agente
            ACLMessage acl = blockingReceive();
//            System.out.println(acl);
            String[] aux = acl.getContent().split(" ");
            double pctTrain = Double.parseDouble(aux[0]);
            String rutaModelo = aux[1];
            double pctTest = test(rutaModelo);
            System.out.println("pctTrain: " + pctTrain);
            System.out.println("pctTest: " + pctTest);
            //10%
            if (Math.abs(pctTrain - pctTest) < 10) {
                System.out.println("envia a predecir");
                new interfacesGUI.inicioGUI().setVisible(true);
                doDelete();
            } else {
                sendMsj("true");
            }

        }

        public void sendMsj(String msj) {
            AID aid = new AID();//identificador
            aid.setLocalName("Train");
            ACLMessage acl = new ACLMessage(ACLMessage.REQUEST);
            acl.addReceiver(aid);
            acl.setSender(getAID());
            acl.setContent(msj);
            acl.setConversationId("2-1");
            acl.setLanguage("ann");
            send(acl);
        }

        public double test(String ruta) {
            double pct = 0;
            try {
                FileReader fileReader = new FileReader(getArguments()[0].toString());
                //crear las instancias
                Instances instances = new Instances(fileReader);
                //definir en la instancias donde esta la etiqueta
                instances.setClassIndex(instances.numAttributes() - 1);

                SerializedClassifier serializedClassifier = new SerializedClassifier();
                serializedClassifier.setModelFile(new File(ruta));
                Classifier mlp = serializedClassifier.getCurrentModel();

                Evaluation evaluation = new Evaluation(instances);
                evaluation.evaluateModel(mlp, instances);
                //System.out.println(evaluation.toMatrixString("Matriz de Conf."));
                //System.out.println(evaluation.toSummaryString());
                pct = evaluation.pctCorrect();
            } catch (Exception ex) {
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pct;
        }

        @Override
        public boolean done() {
            return false;
        }
    }
}
