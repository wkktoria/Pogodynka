package io.github.wkktoria.pogodynka.view;

import io.github.wkktoria.pogodynka.controller.PreferencesController;
import io.github.wkktoria.pogodynka.controller.ResourceController;
import io.github.wkktoria.pogodynka.exception.InvalidLocationException;
import io.github.wkktoria.pogodynka.exception.ReportGenerationProblemException;
import io.github.wkktoria.pogodynka.generator.ReportGenerator;
import io.github.wkktoria.pogodynka.service.PreferencesService;
import org.apache.commons.text.WordUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

class Toolbar extends JToolBar {
    private final ResourceController resourceController;
    private final ReportGenerator reportGenerator;
    private final PreferencesController preferencesController;

    private final JButton configureDefaultLocationButton = new JButton();
    private final JButton generateReportButton = new JButton();

    Toolbar(final ResourceController resourceController,
            final ReportGenerator reportGenerator,
            final PreferencesController preferencesController) {
        this.resourceController = resourceController;
        this.reportGenerator = reportGenerator;
        this.preferencesController = preferencesController;

        setLayout(new BorderLayout());

        setFloatable(false);
        setRollover(true);

        JComboBox<String> languagesList = new JComboBox<>(getLanguages());
        languagesList.addActionListener(e -> {
            int languageIndex = languagesList.getSelectedIndex();

            switch (languageIndex) {
                case 1:
                    preferencesController.setLanguage(PreferencesService.LANGUAGE.ENGLISH);
                    break;
                case 2:
                    preferencesController.setLanguage(PreferencesService.LANGUAGE.POLISH);
                    break;
                case 3:
                    preferencesController.setLanguage(PreferencesService.LANGUAGE.RUSSIAN);
                    break;
                default:
                    return;
            }

            languagesList.setModel(new DefaultComboBoxModel<>(getLanguages()));
            MainFrame.updateLanguage();
        });

        configureDefaultLocationButton.addActionListener(e -> configureDefaultLocation());
        generateReportButton.addActionListener(e -> generateReport());

        add(configureDefaultLocationButton, BorderLayout.WEST);
        add(languagesList, BorderLayout.EAST);
        add(generateReportButton, BorderLayout.CENTER);
    }

    JButton getConfigureDefaultLocationButton() {
        return configureDefaultLocationButton;
    }

    JButton getGenerateReportButton() {
        return generateReportButton;
    }

    private String[] getLanguages() {
        return resourceController.getByKey("availableLanguages").split(",");
    }

    private void generateReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(
                resourceController.getByKey("chooseReportFilename", ResourceController.Case.CAPITALIZED_CASE)
        );
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));

        final int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        if (!selectedFile.getName().toLowerCase().endsWith(".pdf")) {
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".pdf");
        }

        final String location = MainFrame.getMainPanel().getLocationPanel().getLocationLabel().getText();

        if (location == null || location.isEmpty()) {
            return;
        }

        try {
            reportGenerator.generate(selectedFile.getAbsolutePath(), location);
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("reportWasSuccessfullyGenerated", ResourceController.Case.SENTENCE_CASE) + ".",
                    resourceController.getByKey("reportGenerated", ResourceController.Case.CAPITALIZED_CASE),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (ReportGenerationProblemException | InvalidLocationException e) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotGenerateReport", ResourceController.Case.SENTENCE_CASE) + ".",
                    resourceController.getByKey("reportGenerationFailed", ResourceController.Case.CAPITALIZED_CASE),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configureDefaultLocation() {
        String location = JOptionPane.showInputDialog(null,
                resourceController.getByKey("defaultLocation", ResourceController.Case.CAPITALIZED_CASE) + ": ",
                resourceController.getByKey("configureDefaultLocation", ResourceController.Case.CAPITALIZED_CASE),
                JOptionPane.QUESTION_MESSAGE);

        if (location == null || location.isEmpty()) {
            return;
        }

        location = WordUtils.capitalizeFully(location);

        if (preferencesController.setLocation(location)) {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("defaultLocationSetSuccessfullyTo", ResourceController.Case.SENTENCE_CASE) + " " + location + ".",
                    resourceController.getByKey("defaultLocationSetUp", ResourceController.Case.CAPITALIZED_CASE),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    resourceController.getByKey("couldNotSetDefaultLocationTo", ResourceController.Case.SENTENCE_CASE) + " " + location + ".",
                    resourceController.getByKey("invalidLocation", ResourceController.Case.CAPITALIZED_CASE),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
