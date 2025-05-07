package kek.ui;

import kek.math.arithmetic.BigIntegerNumeric;
import kek.math.util.CongruenceSolver;
import kek.math.util.LinearCongruence;
import kek.ui.util.ArrayListModel;
import kek.ui.util.HintTextField;

import javax.swing.*;
import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CongruencesTab extends JPanel {
    public CongruencesTab() {
        GroupLayout tabLayout = new GroupLayout(this);
        setLayout(tabLayout);

        JList<LinearCongruence<BigIntegerNumeric>> congruenceList = new JList<>(new ArrayListModel<>());
        congruenceList.setFont(Fonts.TEXT_FONT);

        CongruenceControls congruenceControls = new CongruenceControls(congruenceList);

        JList<String> computationLog = new JList<>();
        computationLog.setFont(Fonts.TEXT_FONT);

        JButton computeButton = new JButton("Решить");
        computeButton.setFont(Fonts.TEXT_FONT);
        computeButton.addActionListener(event -> {
            ArrayListModel<String> logModel = new ArrayListModel<>();

            try {
                LinearCongruence<BigIntegerNumeric> solution = new CongruenceSolver<>(new CongruenceSolver.SolutionListener<BigIntegerNumeric>() {
                    @Override
                    public void onCongruenceSolution(LinearCongruence<BigIntegerNumeric> congruence, LinearCongruence<BigIntegerNumeric> solution) {
                        logModel.add("%s ⇔ %s".formatted(congruence, solution));
                    }

                    @Override
                    public void onSolutionMerge(LinearCongruence<BigIntegerNumeric> first, LinearCongruence<BigIntegerNumeric> second, LinearCongruence<BigIntegerNumeric> result) {
                        logModel.add("<html><table><tr><th rowspan=\"2\"><font size=\"7\">{</font></th><td>%s</td></tr><tr><td>%s</td></tr></table> ⇔ %s</html>".formatted(first, second, result));
                    }
                }).solve(((ArrayListModel<LinearCongruence<BigIntegerNumeric>>) congruenceList.getModel()).getElements());

                logModel.add("Решение: x = %s + %sk, k в Z".formatted(solution.freeCoefficient(), solution.modulo()));
            } catch (CongruenceSolver.IrreducibleCoefficientException e) {
                logModel.add("У сравнения %s нет решений".formatted(
                        e.getCongruence()
                ));
            } catch (CongruenceSolver.NonCoprimeModuliException e) {
                logModel.add("Модули сравнений %s и %s не являются взаимно простыми".formatted(
                        e.getFirstCongruence(),
                        e.getSecondCongruence()
                ));
            } catch (NoSuchElementException e) {
                logModel.add("Добавьте сравнение, чтобы решить его");
            }

            computationLog.setModel(logModel);
        });

        tabLayout.setHorizontalGroup(tabLayout.createSequentialGroup()
                .addGap(20)
                .addGroup(tabLayout.createParallelGroup()
                        .addComponent(congruenceControls, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)
                        .addComponent(congruenceList)
                        .addComponent(computeButton))
                .addGap(20)
                .addComponent(computationLog, GroupLayout.PREFERRED_SIZE, 100, Integer.MAX_VALUE));

        tabLayout.setVerticalGroup(tabLayout.createParallelGroup()
                .addGroup(tabLayout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(congruenceControls, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(20)
                        .addComponent(congruenceList)
                        .addGap(20, 20, Integer.MAX_VALUE)
                        .addComponent(computeButton)
                        .addGap(20))
                .addComponent(computationLog, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE));

        tabLayout.linkSize(SwingConstants.HORIZONTAL, congruenceControls, congruenceList);
    }

    private static final class CongruenceControls extends JPanel {
        private static final Pattern LINEAR_CONGRUENCE_PATTERN = Pattern.compile("([-+]?\\d+)?x\\s*[=≡]\\s*([-+]?\\d+)\\s*\\(mod\\s+([-+]?\\d+)\\)");

        private final JList<LinearCongruence<BigIntegerNumeric>> congruenceList;
        private final JTextField congruenceField;

        public CongruenceControls(JList<LinearCongruence<BigIntegerNumeric>> congruenceList) {
            this.congruenceList = congruenceList;

            GroupLayout controlsLayout = new GroupLayout(this);
            setLayout(controlsLayout);

            congruenceField = new HintTextField("2x = 3 (mod 4)");
            congruenceField.setFont(Fonts.TEXT_FONT);
            congruenceField.addActionListener(event -> addCongruenceAction());

            JButton removeCongruenceButton = new JButton("-");
            removeCongruenceButton.setFont(Fonts.TEXT_FONT);
            removeCongruenceButton.addActionListener(event ->
                    ((ArrayListModel<LinearCongruence<BigIntegerNumeric>>) congruenceList.getModel()).remove(congruenceList.getSelectedIndices())
            );

            JButton addCongruenceButton = new JButton("+");
            addCongruenceButton.setFont(Fonts.TEXT_FONT);
            addCongruenceButton.addActionListener(event -> addCongruenceAction());

            controlsLayout.setHorizontalGroup(controlsLayout.createSequentialGroup()
                    .addComponent(removeCongruenceButton)
                    .addGap(20)
                    .addComponent(addCongruenceButton)
                    .addGap(20)
                    .addComponent(congruenceField));

            controlsLayout.setVerticalGroup(controlsLayout.createParallelGroup()
                    .addComponent(removeCongruenceButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(addCongruenceButton, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(congruenceField, GroupLayout.Alignment.CENTER));

            controlsLayout.linkSize(SwingConstants.HORIZONTAL, removeCongruenceButton, addCongruenceButton);
            controlsLayout.linkSize(SwingConstants.VERTICAL, removeCongruenceButton, addCongruenceButton, congruenceField);
        }

        private void addCongruenceAction() {
            Matcher matcher = LINEAR_CONGRUENCE_PATTERN.matcher(congruenceField.getText().strip());

            if (!matcher.matches())
                return;

            String xCoefficient = matcher.group(1);

            ((ArrayListModel<LinearCongruence<BigIntegerNumeric>>) congruenceList.getModel()).add(new LinearCongruence<>(
                    xCoefficient != null ? new BigIntegerNumeric(new BigInteger(xCoefficient)) : new BigIntegerNumeric(BigInteger.ONE),
                    new BigIntegerNumeric(new BigInteger(matcher.group(2))),
                    new BigIntegerNumeric(new BigInteger(matcher.group(3)))
            ));
        }
    }
}
