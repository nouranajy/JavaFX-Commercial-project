package com.example.commercialjavaproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsersController {

    @FXML private TableView<UserModel> usersTable;
    @FXML private TableColumn<UserModel, Long> colId;
    @FXML private TableColumn<UserModel, String> colFirstName;
    @FXML private TableColumn<UserModel, String> colLastName;
    @FXML private TableColumn<UserModel, String> colEmail;
    @FXML private TableColumn<UserModel, String> colRole;
    @FXML private TableColumn<UserModel, String> colStatus;
    @FXML private TableColumn<UserModel, Void> colActions;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> roleFilterCombo;

    @FXML private Label pageInfoLabel;
    @FXML private Button prevBtn;
    @FXML private Button nextBtn;

    private ObservableList<UserModel> masterData = FXCollections.observableArrayList();
    private FilteredList<UserModel> filteredData;

    private int currentPage = 1;
    private final int rowsPerPage = 10;

    @FXML
    public void initialize() {
        // Responsive Columns
        colId.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.05));
        colFirstName.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.20)); // Wider for Avatar
        colLastName.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.15));
        colEmail.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.22));
        colRole.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.12));
        colStatus.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.10));
        colActions.prefWidthProperty().bind(usersTable.widthProperty().multiply(0.16));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Setup Premium Avatar Name Column
        setupNameColumnWithAvatar();

        roleFilterCombo.setItems(FXCollections.observableArrayList("ALL", "ADMIN", "MANAGER", "CLIENT"));
        roleFilterCombo.setValue("ALL");

        filteredData = new FilteredList<>(masterData, p -> true);
        usersTable.setItems(filteredData);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        roleFilterCombo.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        setupActionColumn();
        loadUsers();
    }

    private void setupNameColumnWithAvatar() {
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colFirstName.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);
                if (empty || name == null) {
                    setGraphic(null);
                } else {
                    UserModel user = getTableView().getItems().get(getIndex());
                    String initial = name.substring(0, 1).toUpperCase();

                    // Avatar Circle
                    StackPane avatar = new StackPane();
                    avatar.setStyle("-fx-background-color: #E0E7FF; -fx-background-radius: 50; -fx-min-width: 32; -fx-min-height: 32;");
                    Label initialLabel = new Label(initial);
                    initialLabel.setStyle("-fx-text-fill: #4F46E5; -fx-font-weight: bold; -fx-font-size: 14px;");
                    avatar.getChildren().add(initialLabel);

                    Label nameLabel = new Label(name);
                    nameLabel.setStyle("-fx-text-fill: #0F172A; -fx-font-weight: bold; -fx-font-size: 13px;");

                    HBox container = new HBox(10, avatar, nameLabel);
                    container.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    setGraphic(container);
                }
            }
        });
    }

    private void applyFilters() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        String selectedRole = roleFilterCombo.getValue();

        filteredData.setPredicate(user -> {
            boolean matchesSearch = searchText.isEmpty() ||
                    user.getFirstName().toLowerCase().contains(searchText) ||
                    user.getLastName().toLowerCase().contains(searchText) ||
                    user.getEmail().toLowerCase().contains(searchText);

            boolean matchesRole = selectedRole == null || selectedRole.equals("ALL") ||
                    user.getRoleName().equals(selectedRole);

            return matchesSearch && matchesRole;
        });

        currentPage = 1;
        updatePagination();
    }

    public void loadUsers() {
        masterData.clear();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT u.user_id, u.first_name, u.last_name, u.email, u.is_active, r.role_name " +
                    "FROM users u JOIN roles r ON u.role_id = r.role_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                masterData.add(new UserModel(
                        rs.getLong("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("role_name"),
                        rs.getBoolean("is_active") ? "Active" : "Inactive"
                ));
            }
            applyFilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadUsers();
    }

    private void updatePagination() {
        int totalItems = filteredData.size();
        int maxPage = (int) Math.ceil((double) totalItems / rowsPerPage);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > maxPage) currentPage = maxPage;

        int fromIndex = (currentPage - 1) * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, totalItems);

        if (totalItems == 0) {
            pageInfoLabel.setText("No entries found");
            prevBtn.setDisable(true);
            nextBtn.setDisable(true);
            return;
        }

        pageInfoLabel.setText("Showing " + (fromIndex + 1) + " to " + toIndex + " of " + totalItems + " entries");
        prevBtn.setDisable(currentPage == 1);
        nextBtn.setDisable(currentPage == maxPage);
    }

    @FXML
    public void prevPage() {
        if (currentPage > 1) {
            currentPage--;
            updatePagination();
        }
    }

    @FXML
    public void nextPage() {
        int totalItems = filteredData.size();
        int maxPage = (int) Math.ceil((double) totalItems / rowsPerPage);
        if (currentPage < maxPage) {
            currentPage++;
            updatePagination();
        }
    }

    private void setupActionColumn() {
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox container = new HBox(8, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4F46E5; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 12px;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 12px;");
                container.setAlignment(javafx.geometry.Pos.CENTER);
                editBtn.setOnAction(e -> showEditForm(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> deleteUser(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    @FXML
    public void showAddForm() { openUserForm(null); }
    private void showEditForm(UserModel user) { openUserForm(user); }

    private void openUserForm(UserModel user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/user-form.fxml"));
            Parent root = loader.load();
            UserFormController controller = loader.getController();
            controller.setParentController(this);
            controller.setUserToEdit(user);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle(user == null ? "Create User" : "Edit User");
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser(UserModel user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection conn = DatabaseManager.getConnection()) {
                    String sql = "DELETE FROM users WHERE user_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setLong(1, user.getId());
                    pstmt.executeUpdate();
                    loadUsers();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static class UserModel {
        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final String roleName;
        private final String status;

        public UserModel(Long id, String firstName, String lastName, String email, String roleName, String status) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.roleName = roleName;
            this.status = status;
        }
        public Long getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getRoleName() { return roleName; }
        public String getStatus() { return status; }
    }
}