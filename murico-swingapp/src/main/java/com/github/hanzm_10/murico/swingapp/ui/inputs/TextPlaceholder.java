package com.github.hanzm_10.murico.swingapp.ui.inputs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.jetbrains.annotations.NotNull;

import com.github.hanzm_10.murico.swingapp.lib.utils.ColorUtils;

public class TextPlaceholder extends JLabel implements FocusListener, DocumentListener {
    private JTextComponent textComponent;
    private Document document;
    private Show show;
    protected boolean isDestroyed;

    public enum Show {
        ALWAYS,
        FOCUS_GAINED,
        FOCUS_LOST
    }

    public TextPlaceholder(@NotNull final String placeholderText, JTextComponent textComponent, Show show) {
        this.show = show;
        this.textComponent = textComponent;
        document = textComponent.getDocument();

        setText(placeholderText);
        setFont(textComponent.getFont());
        setForeground(ColorUtils.changeAlpha(textComponent.getForeground(), 0.5f));
        //setBorder(new EmptyBorder(textComponent.getInsets()));
        setHorizontalAlignment(JLabel.LEADING);

        textComponent.addFocusListener(this);
        document.addDocumentListener(this);

        textComponent.setLayout(new BorderLayout());
        textComponent.add(this);
        displayIfPossible();
    }

    public TextPlaceholder(@NotNull final String placeholderText, JTextComponent textComponent) {
        new TextPlaceholder(placeholderText, textComponent, Show.ALWAYS);
    }

    public boolean getIsDestroyed() {
        return isDestroyed;
    }

    public void destroy() {
        if (isDestroyed) {
            return;
        }

        textComponent.removeFocusListener(this);
        document.removeDocumentListener(this);
        document = null;
        textComponent = null;
        isDestroyed = true;
    }

    public void changeFontStyle(int fontStyle) {
        setFont(getFont().deriveFont(fontStyle));
    }

    public void displayIfPossible() {
        if (document.getLength() != 0) {
            setVisible(false);
            return;
        }

        if (show == Show.FOCUS_GAINED) {
            if (!textComponent.hasFocus()) {
                setVisible(false);
            } else {
                setVisible(true);
            }

            return;
        }

        if (show == Show.FOCUS_LOST) {
            if (textComponent.hasFocus()) {
                setVisible(false);
            } else {
                setVisible(true);
            }

            return;
        }

        setVisible(true);;
    }

    @Override
    public void focusGained(FocusEvent e) {
        displayIfPossible();
    }

    @Override
    public void focusLost(FocusEvent e) {
        displayIfPossible();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        displayIfPossible();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        displayIfPossible();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }
}

