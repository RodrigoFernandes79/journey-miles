package com.br.rodrigo.jornadamilhas.services;

import com.br.rodrigo.jornadamilhas.domains.destination.Destination;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

@Service
public class OpenApiService {
    private static final String JOURNEY_MILES_KEY = "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";

    public String generateTextDescription(String nameDestination) {
        OpenAiService openAiService = new OpenAiService(JOURNEY_MILES_KEY);
        Destination destination = new Destination();
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("""
                        Make a brief summary about the following place: 
                        """ + nameDestination +
                        """ 
                                emphasizing why this place is amazing.
                                Use informal Portuguese language and up to 100 characters maximum in each paragraph.
                                Create 2 paragraphs in this summary.
                                Then translate the text into English.                                                                                                                                                                           
                                        """)
                .maxTokens(1000)
                .build();
        var response = openAiService.createCompletion(completionRequest);

        return response.getChoices().get(0).getText();
    }
}
