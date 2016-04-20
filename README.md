# TreeAnno

TreeAnno is an annotation tool develop to annotate trees on top of texts. It 
is designed for annotating longer text segments and allows fast annotation
through the use of keyboard commands.

## User roles
TreeAnno distinguishes several user roles:

- General admin:
  Is allowed to do everything
- Project admin:
  Has admin rights for one or more project(s). This includes uploading documents
  to this project, deleting documents, assigning reviewing tasks and 
  comparing/merging the results. The project admin also has the right to edit 
  the master document, which is used as a basis for the annotators
- Annotator: 
  Can edit (annotate) the documents in the project(s) he/she is assigned to.
  Each annotator gets their own copy upon the first edit.

## Workflow 
TreeAnno was built with the following workflow in mind:

1. Uploading of documents
   Documents are raw plain text and get tokenised and sentence split 
   automatically.
2. Annotation preparation
   The project administrator edits the master document before annotation 
   starts to, e.g., fix the sentence splitting.
3. Annotation
   Each annotator annotates individually, doing segmentation changes and 
   tree changes simultaneously.