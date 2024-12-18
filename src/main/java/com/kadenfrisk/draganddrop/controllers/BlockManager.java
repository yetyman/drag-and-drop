package com.kadenfrisk.draganddrop.controllers;

import com.kadenfrisk.draganddrop.models.Block;
import com.kadenfrisk.draganddrop.models.blocks.control.StartBlock;
import com.kadenfrisk.draganddrop.models.blocks.control.StopBlock;
import com.kadenfrisk.draganddrop.models.blocks.control.WaitBlock;
import com.kadenfrisk.draganddrop.models.blocks.gui.DialogBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.BooleanExpressionBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.ForLoopBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.IfBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.IfElseBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.SwitchBlock;
import com.kadenfrisk.draganddrop.models.blocks.logic.WhileLoopBlock;
import com.kadenfrisk.draganddrop.models.blocks.operation.MathBlock;
import com.kadenfrisk.draganddrop.models.blocks.operation.VariableBlock;
import com.kadenfrisk.draganddrop.models.blocks.sensors.SensorBlock;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class BlockManager {

    private static final BlockManager instance = new BlockManager();
    private final ArrayList<StartBlock> startBlocks;
    private final ArrayList<Block> blocks;
    private boolean isStopRequested = false;

    private BlockManager() {
        // Private constructor to prevent instantiation
        startBlocks = new ArrayList<>();
        blocks = new ArrayList<>();
    }

    public static BlockManager getInstance() {
        return instance;
    }

    public void run() {
        System.out.println("Running BlockManager");

        if (startBlocks.isEmpty()) {
            System.out.println("No start blocks found");
            // Simple GUI dialog to inform user that there are no start blocks
            JOptionPane.showMessageDialog(
                null,
                "No start blocks found",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Logic to call all start blocks run() function
        // If there is only one, simply call it. If there are multiple, call them with multithreading
        if (startBlocks.size() == 1) {
            startBlocks.getFirst().run();
        } else {
            for (StartBlock block : startBlocks) {
                new Thread(block::run).start();
            }
        }
    }

    public void addStartBlock(StartBlock block) {
        startBlocks.add(block);
    }

    public void removeStartBlock(StartBlock startBlock) {
        startBlocks.remove(startBlock);
    }

    public Block createBlockByName(String blockName) {
        return switch (blockName) {
            case "If Block" -> new IfBlock();
            case "If/Else Block" -> new IfElseBlock();
            case "While Loop Block" -> new WhileLoopBlock();
            case "For Loop Block" -> new ForLoopBlock();
            case "Switch Block" -> new SwitchBlock();
            case "Boolean Expression Block" -> new BooleanExpressionBlock();
            case "Start Block" -> new StartBlock();
            case "Stop Block" -> new StopBlock();
            case "Wait Block" -> new WaitBlock();
            case "Math Block" -> new MathBlock();
            case "Variable Block" -> new VariableBlock();
            case "Sensor" -> new SensorBlock();
            case "Dialog Block" -> new DialogBlock();
            default -> {
                System.out.println(
                    "Unknown block type, defaulting to example block"
                );
                yield new Block();
            }
        };
    }

    public void addBlock(Block newBlock) {
        blocks.add(newBlock);
    }

    public void clearStopRequest() {
        isStopRequested = false;
    }

    public void requestStop() {
        isStopRequested = true;
    }
}
