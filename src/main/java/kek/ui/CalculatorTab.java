package kek.ui;

import kek.math.LogEntry;
import kek.math.Operation;
import kek.math.arithmetic.BigIntegerNumeric;
import kek.math.ops.ExtendedGcdOperation;
import kek.math.ops.GcdOperation;
import kek.math.ops.ModuloOperation;
import kek.math.ops.SimpleArithmeticOperation;
import kek.ui.util.ArrayListModel;
import kek.ui.util.ItemSwitcher;

import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class CalculatorTab extends JPanel {
    private static final List<Algorithm> ALGORITHMS = Stream.<Operation<BigIntegerNumeric>>of(
            new GcdOperation<>(),
            new ExtendedGcdOperation<>(),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.ADDITION, false),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.SUBTRACTION, false),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.MULTIPLICATION, false),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.ADDITION, true),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.SUBTRACTION, true),
            new SimpleArithmeticOperation<>(SimpleArithmeticOperation.Type.MULTIPLICATION, true),
            new ModuloOperation<>()
    ).map(Algorithm::new).toList();

    private final Map<Algorithm, ControlPanel> controlPanels;

    public CalculatorTab() {
        this(ALGORITHMS);
    }

    public CalculatorTab(List<Algorithm> algorithms) {
        this.controlPanels = algorithms.stream().map(ControlPanel::new).collect(Collectors.toUnmodifiableMap(
                ControlPanel::getAlgorithmOption,
                Function.identity()
        ));

        GroupLayout tabLayout = new GroupLayout(this);
        setLayout(tabLayout);

        JComboBox<Algorithm> algorithmChooser = new JComboBox<>();
        algorithmChooser.setFont(Fonts.TEXT_FONT);
        algorithms.forEach(algorithmChooser::addItem);
        algorithmChooser.addItemListener(new ItemSwitcher<Algorithm>() {
            @Override
            protected void onItemReplace(Algorithm previous, Algorithm current) {
                if (previous != null) {
                    tabLayout.replace(
                            controlPanels.get(previous),
                            controlPanels.get(current)
                    );
                }
            }
        });

        JList<String> computationLog = new JList<>();
        computationLog.setFont(Fonts.TEXT_FONT);

        JButton computeButton = new JButton("Вычислить");
        computeButton.setFont(Fonts.TEXT_FONT);
        computeButton.addActionListener(event -> {
            List<LogEntry> log = controlPanels.get((Algorithm) algorithmChooser.getSelectedItem()).invokeAlgorithm();
            computationLog.setModel(new ArrayListModel<>(log.stream().map(LogEntry::toLogString).toList()));
        });

        ControlPanel currentPanel = controlPanels.get((Algorithm) algorithmChooser.getSelectedItem());

        tabLayout.setHorizontalGroup(tabLayout.createSequentialGroup()
                .addGap(20)
                .addGroup(tabLayout.createParallelGroup()
                        .addComponent(algorithmChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(currentPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, 20 * Fonts.getMaxDigitWidth(getFontMetrics(Fonts.TEXT_FONT)))
                        .addComponent(computeButton))
                .addGap(20)
                .addComponent(computationLog, GroupLayout.PREFERRED_SIZE, 100, Integer.MAX_VALUE));

        tabLayout.setVerticalGroup(tabLayout.createParallelGroup()
                .addGroup(tabLayout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(algorithmChooser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(currentPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, Integer.MAX_VALUE)
                        .addComponent(computeButton)
                        .addGap(20))
                .addComponent(computationLog, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE));
    }

    private static final class ControlPanel extends JPanel {
        private final Algorithm algorithm;

        public ControlPanel(Algorithm algorithm) {
            this.algorithm = algorithm;

            GroupLayout controlsLayout = new GroupLayout(this);
            setLayout(controlsLayout);

            GroupLayout.SequentialGroup verticalGroup = controlsLayout.createSequentialGroup();
            controlsLayout.setVerticalGroup(verticalGroup);

            GroupLayout.ParallelGroup horizontalGroup = controlsLayout.createParallelGroup();
            controlsLayout.setHorizontalGroup(horizontalGroup);

            for (Operation.Parameter parameter : algorithm.operation().getParameters()) {
                JLabel parameterLabel = new JLabel(parameter.description());
                JFormattedTextField parameterInput = new JFormattedTextField(BigInteger.ZERO);

                parameterLabel.setFont(Fonts.TEXT_FONT);
                parameterInput.setFont(Fonts.TEXT_FONT);

                verticalGroup
                        .addGap(20)
                        .addComponent(parameterLabel)
                        .addComponent(parameterInput);

                horizontalGroup
                        .addComponent(parameterLabel)
                        .addComponent(parameterInput);
            }
        }

        public Algorithm getAlgorithmOption() {
            return algorithm;
        }

        public List<LogEntry> invokeAlgorithm() {
            return algorithm.operation().apply(
                    Arrays.stream(getComponents())
                            .filter(c -> c instanceof JFormattedTextField)
                            .map(c -> new BigIntegerNumeric((BigInteger) ((JFormattedTextField) c).getValue()))
                            .toList()
            );
        }
    }

    public record Algorithm(Operation<BigIntegerNumeric> operation) {
        @Override
        public String toString() {
            return operation.getName();
        }
    }
}
