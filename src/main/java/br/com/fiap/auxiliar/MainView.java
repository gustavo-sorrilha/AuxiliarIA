package br.com.fiap.auxiliar;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
class MainView extends VerticalLayout {

    @Autowired
    ChatService service;

    MainView() {
        var chatBar = new HorizontalLayout();
        chatBar.setWidthFull();

        var messagePanel = new MessageList();
        messagePanel.setHeightFull();
        messagePanel.setWidthFull();
        messagePanel.getStyle().set("background-color", "#f0f0f0");

        TextField textField = new TextField();
        textField.setPlaceholder("Digite uma pergunta para o auxiliar!");
        textField.setClearButtonVisible(true);
        textField.setWidthFull();
        textField.setPrefixComponent(VaadinIcon.CHAT.create());

        Button sendButton = new Button(new Icon(VaadinIcon.PAPERPLANE));
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.setAriaLabel("Send message");
        sendButton.addClickListener(event -> {
            MessageListItem userMessage = new MessageListItem(
                textField.getValue(),
                LocalDateTime.now().atOffset(ZoneOffset.ofHours(-3)).toInstant(),
                "Beneficiario"
            );

            var response = service.sendMessage(textField.getValue());
            MessageListItem auxiliarMessage = new MessageListItem(
                response,
                LocalDateTime.now().atOffset(ZoneOffset.ofHours(-3)).toInstant(),
                "Auxiliar"
            );

            messagePanel.setItems(List.of(userMessage, auxiliarMessage));
        });

        chatBar.add(textField);
        chatBar.add(sendButton);

        setHeightFull();

        add(new H1("Chat de Tarefas do dia a dia"));
        add(messagePanel);
        add(chatBar);
    }
}