package ui.manager.catalog;

import business.productCatalog.Category;
import business.productCatalog.Product;
import dao.CategoryDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EditProductUI extends JDialog {

    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField descriptionField;
    private JComboBox categoryComboBox;
    private ProductCatalogUI parent;
    private Object[] productDetails;

    public EditProductUI(ProductCatalogUI parent, Object[] productDetails) {
        super(parent, "Edit Product", true);
        this.productDetails = productDetails;
        this.parent = parent;
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(6, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField(productDetails[1].toString());
        panel.add(nameField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField(productDetails[2].toString());
        panel.add(descriptionField);

        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(productDetails[3].toString());
        panel.add(quantityField);

        panel.add(new JLabel("Price($):"));
        priceField = new JTextField(productDetails[4].toString());
        panel.add(priceField);

        panel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>();
        panel.add(categoryComboBox);

        loadCategories();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editProduct();
            }
        });
        panel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(cancelButton);

        add(panel);
    }

    private void loadCategories() {
        Category c = new Category();
        List<Category> allCategories = c.loadCategories();

        for (Category category : allCategories) {
            categoryComboBox.addItem(category.getName());
        }
    }

    private void editProduct() {
        String name = nameField.getText();
        if (priceField.getText().isEmpty() || !isNumeric(priceField.getText())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric price",
                    "Invalid Price",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        double price = Double.parseDouble(priceField.getText());
        if (quantityField.getText().isEmpty() || !isNumeric(quantityField.getText())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric quantity",
                    "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        int quantity = Integer.parseInt(quantityField.getText());
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter name",
                    "Name Field Blank",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (price < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter price greater than 0",
                    "Invalid Price",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        if (quantity < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Quantity cannot be negative",
                    "Invalid Quantity",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String description = descriptionField.getText();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter description",
                    "Description Blank",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        String selectedCategory = categoryComboBox.getSelectedItem().toString();
        Category c = new Category();
        Product product = new Product(Integer.parseInt(productDetails[0].toString()), name, description, quantity, price, c.getCategoryCode(selectedCategory));
        c.editProduct(product);
        parent.updateTable();
        dispose();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

