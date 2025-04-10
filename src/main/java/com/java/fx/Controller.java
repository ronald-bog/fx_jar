package com.java.fx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class Controller implements Initializable {

    @Autowired
    private Repository repo;

    private int id;

    private Model item;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ListView<Model> list;

    @FXML
    private TextField txtId, txtName, txtTel;

    @FXML
    private Button btnCreate, btnClear, btnUpdate, btnDelete;

    @FXML
    private Label lblTotal, lblWarning;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtId.setDisable(true);
        list();
        list.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, newValue) -> {
                            if (newValue != null) {
                                id = newValue.getId();
                                item = item();
                                txtId.setText(String.valueOf(item.getId()));
                                txtName.setText(item.getNombre());
                                txtTel.setText(item.getTelefono());
                                btnCreate.setVisible(false);
                                btnClear.setVisible(true);
                                btnUpdate.setVisible(true);
                                btnDelete.setVisible(true);
                                lblWarning.setVisible(false);
                            }
                        }
                );
    }

    public Model item() {
        return repo.findById(id).get();
    }

    public void list() {
        list.setItems(FXCollections.observableArrayList(repo.findAll()));
        lblTotal.setText(String.valueOf(list.getItems().size()));
    }

    @FXML
    public void saved() {
        if (!txtName.getText().trim().isEmpty()) {
            Model mod = new Model();
            mod.setNombre(txtName.getText());
            mod.setTelefono(txtTel.getText());
            repo.save(mod);
            list();
            clear();
        } else {
            lblWarning.setVisible(true);
        }
    }

    @FXML
    public void edit(){
        item.setNombre(txtName.getText());
        item.setTelefono(txtTel.getText());
        repo.save(item);
        list();
    }

    @FXML
    public void clear() {
        txtId.clear();
        txtName.clear();
        txtTel.clear();
//        anchorPane.getChildren().forEach( node -> {
//            if (node instanceof  TextField)((TextField) node).clear();
//        });
        btnCreate.setVisible(true);
        btnClear.setVisible(false);
        btnUpdate.setVisible(false);
        btnDelete.setVisible(false);
    }

    public void delete(){
        repo.delete(item);
        clear();
        list();
    }
}
