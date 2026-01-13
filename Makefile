# Makefile pour la compilation du rapport LaTeX
# Usage: make ou make pdf

MAIN = rapport-projet
TEX = $(MAIN).tex
PDF = $(MAIN).pdf

# Commandes LaTeX
LATEX = pdflatex
BIBTEX = bibtex

# Options de compilation
LATEX_OPTS = -interaction=nonstopmode -file-line-error

.PHONY: all clean pdf view help

# Compilation par défaut
all: pdf

# Compilation complète avec références croisées
pdf: $(TEX)
	@echo "Compilation 1/4..."
	$(LATEX) $(LATEX_OPTS) $(TEX)
	@echo "Compilation 2/4..."
	$(LATEX) $(LATEX_OPTS) $(TEX)
	@echo "Compilation terminée: $(PDF)"
	@echo "Pour voir le PDF, utilisez: make view"

# Compilation rapide (une seule passe, sans références)
quick: $(TEX)
	$(LATEX) $(LATEX_OPTS) $(TEX)

# Nettoyage des fichiers temporaires
clean:
	@echo "Nettoyage des fichiers temporaires..."
	rm -f *.aux *.log *.out *.toc *.lof *.lot *.bbl *.blg *.synctex.gz
	@echo "Nettoyage terminé"

# Nettoyage complet (inclut les fichiers PDF)
cleanall: clean
	rm -f $(PDF)

# Ouvrir le PDF (Windows)
view: $(PDF)
	@echo "Ouverture du PDF..."
	@if [ -f $(PDF) ]; then \
		if command -v start > /dev/null; then \
			start $(PDF); \
		elif command -v xdg-open > /dev/null; then \
			xdg-open $(PDF); \
		elif command -v open > /dev/null; then \
			open $(PDF); \
		fi \
	fi

# Aide
help:
	@echo "Makefile pour la compilation du rapport LaTeX"
	@echo ""
	@echo "Commandes disponibles:"
	@echo "  make       - Compilation complète (recommandé)"
	@echo "  make pdf   - Compilation complète"
	@echo "  make quick - Compilation rapide (une seule passe)"
	@echo "  make clean - Nettoyer les fichiers temporaires"
	@echo "  make cleanall - Nettoyer tout (inclut le PDF)"
	@echo "  make view  - Ouvrir le PDF généré"
	@echo "  make help  - Afficher cette aide"
	@echo ""
	@echo "Note: Les diagrammes doivent être en format PDF dans conception-diagrams/"
