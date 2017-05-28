package com.flockinger.poppynotes.notesService.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="archivedNote")
public class ArchivedNote extends Note {
}
