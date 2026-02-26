# Backlog

## Fait
- [x] Implementer l'ecran d'edition d'un client (accessible depuis l'ecran detail, pre-remplissage et sauvegarde en memoire).
- [x] Creer les fichiers de tests unitaires par fonctionnalite: `CustomerListTest`, `CustomerDetailTest`, `CustomerAddEditTest`.
- [x] Ecrire les tests unitaires JUnit pour `generateDate`, `toHumanDate`, `isNewCustomer`.
- [x] Ecrire 3 tests d'integration Android dans `androidTest`:
  - [x] Verification de 5 elements au demarrage.
  - [x] Verification de l'Intent detail + extra `customer_id=1` au clic sur le 1er element.
  - [x] Verification Compose de l'ecran detail pour Alice (`id=1`).
- [x] Generer les rapports de couverture (Android test coverage + JaCoCo HTML).

## A faire
- [ ] Augmenter la couverture de `AddCustomerActivity` (actuellement non couverte par les tests d'integration).
- [ ] Ajouter un test d'integration du flux complet d'edition (ouvrir detail -> edit -> save -> retour liste/detail avec donnees mises a jour).
