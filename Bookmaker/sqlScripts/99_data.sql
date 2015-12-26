DELETE FROM matchEvent;
DELETE FROM stage;
DELETE FROM team;
DELETE FROM user;

INSERT INTO team (id, teamNr, nameEn, nameDe, nameFr, nameIt, countryCode) VALUES
	(1, 'A1', 'France', 'Frankreich', 'France_FR', 'France_IT', 'fra'),
	(2, 'A2', 'Romania', 'Rumänien', 'Romania_FR', 'Romania_IT', 'rou'),
	(3, 'A3', 'Albania', 'Albanien', 'Albania_FR', 'Albania_IT',  'alb'),
	(4, 'A4', 'Switzerland', 'Schweiz', 'Switzerland_FR', 'Switzerland_IT','sui'),

	(5, 'B1', 'England', 'England', 'England_FR', 'England_IT', 'eng'),
	(6, 'B2', 'Russia', 'Russland', 'Russia_FR', 'Russia_IT', 'rus'),
	(7, 'B3', 'Wales', 'Wales', 'Wales_FR', 'Wales_IT', 'wal'),
	(8, 'B4', 'Slovakia', 'Slowakei', 'Slovakia_FR', 'Slovakia_IT', 'svk'),

	(9, 'C1', 'Germany', 'Deutschland', 'Germany_FR', 'Germany_IT', 'ger'),
	(10, 'C2', 'Ukraine', 'Ukraine', 'Ukraine_FR', 'Ukraine_IT', 'ukr'),
	(11, 'C3', 'Poland', 'Polen', 'Poland_FR', 'Poland_IT', 'pol'),
	(12, 'C4', 'Northern Ireland', 'Nordirland', 'Northern Ireland_FR', 'Northern Ireland_IT', 'nir'),

	(13, 'D1', 'Spain', 'Spanien', 'Spain_FR', 'Spain_IT', 'esp'),
	(14, 'D2', 'Czech Republic', 'Tschechische Rep.', 'Czech Republic_FR', 'Czech Republic_IT', 'cze'),
	(15, 'D3', 'Turkey', 'Türkei', 'Turkey_FR', 'Turkey_IT', 'tur'),
	(16, 'D4', 'Croatia', 'Kroatien', 'Croatia_FR', 'Croatia_IT', 'cro'),

	(17, 'E1', 'Belgium', 'Belgien', 'Belgium_FR', 'Belgium_IT', 'bel'),
	(18, 'E2', 'Italy', 'Italien', 'Italy_FR', 'Italy_IT', 'ita'),
	(19, 'E3', 'Republic of Ireland', 'Republik Irland', 'Republic of Ireland_FR', 'Republic of Ireland_IT', 'irl'),
	(20, 'E4', 'Sweden', 'Schweden', 'Sweden_FR', 'Sweden_IT', 'swe'),

	(21, 'F1', 'Portugal', 'Portugal', 'Portugal_FR', 'Portugal_IT', 'por'),
	(22, 'F2', 'Iceland', 'Island', 'Iceland_FR', 'Iceland_IT', 'isl'),
	(23, 'F3', 'Austria', 'Österreich', 'Austria_FR', 'Austria_IT', 'aut'),
	(24, 'F4', 'Hungary', 'Ungarn', 'Hungary_FR', 'Hungary_IT', 'hun');

INSERT INTO stage(id, nameEn, nameDe, nameFr, nameIt) VALUES
	(1, 'Group stage', 'Gruppenphase', 'Group stage_FR', 'Group stage_IT'),
	(2, 'Round of 16', 'Achtelfinale', 'Round of 16_FR', 'Round of 16_IT'),
	(3, 'Quarter-finals', 'Viertelfinale', 'Quarter-finals_FR', 'Quarter-finals_IT'),
	(4, 'Semi-finals', 'Halbfinale', 'Semi-finals_FR', 'Semi-finals_IT'),
	(5, 'Final', 'Finale', 'Final_FR', 'Final_IT');

INSERT INTO matchEvent (id, matchEventNr, stageId, matchEventGroup, matchEventDateTime, teamHomeId, teamAwayId) VALUES
	(1, 1, 1, 'A', '2016-06-10 21:00:00', 1, 2), # A1 - A2
	(2, 2, 1, 'A', '2016-06-11 15:00:00', 3, 4), # A3 - A4
	(3, 3, 1, 'B', '2016-06-11 18:00:00', 7, 8), # B3 - B4
	(4, 4, 1, 'B', '2016-06-11 21:00:00', 5, 6), # B1 - B2
	(5, 5, 1, 'D', '2016-06-12 15:00:00', 15, 16), # D3 - D4
	(6, 6, 1, 'C', '2016-06-12 18:00:00', 11, 12), # C3 - C4
	(7, 7, 1, 'C', '2016-06-12 21:00:00', 9, 10), # C1 - C2
	(8, 8, 1, 'D', '2016-06-13 15:00:00', 13, 14), # D1 - D2
	(9, 9, 1, 'E', '2016-06-13 18:00:00', 19, 20), # E3 - E4
	(10, 10, 1, 'E', '2016-06-13 21:00:00', 17, 18), # E1 - E2
	(11, 11, 1, 'F', '2016-06-14 18:00:00', 19, 20), # F3 - F4
	(12, 12, 1, 'F', '2016-06-14 21:00:00', 21, 22), # F1 - F2

	(13, 13, 1, 'B', '2016-06-15 15:00:00', 6, 8), # B2 - B4
	(14, 14, 1, 'A', '2016-06-15 18:00:00', 2, 4), # A2 - A4
	(15, 15, 1, 'A', '2016-06-15 21:00:00', 1, 3), # A1 - A3
	(16, 16, 1, 'B', '2016-06-16 15:00:00', 5, 7), # B1 - B3
	(17, 17, 1, 'C', '2016-06-16 18:00:00', 10, 12), # C2 - C4
	(18, 18, 1, 'C', '2016-06-16 21:00:00', 9, 11), # C1 - C3
	(19, 19, 1, 'E', '2016-06-17 15:00:00', 18, 20), # E2 - E4
	(20, 20, 1, 'D', '2016-06-17 18:00:00', 14, 16), # D2 - D4
	(21, 21, 1, 'D', '2016-06-17 21:00:00', 13, 15), # D1 - D3
	(22, 22, 1, 'E', '2016-06-18 15:00:00', 17, 19), # E1 - E3
	(23, 23, 1, 'F', '2016-06-18 18:00:00', 22, 24), # F2 - F4
	(24, 24, 1, 'F', '2016-06-18 21:00:00', 21, 23), # F1 - F3

	(25, 25, 1, 'A', '2016-06-19 21:00:00', 2, 3), # A2 - A3
	(26, 26, 1, 'A', '2016-06-19 21:00:00', 4, 1), # A4 - A1
	(27, 27, 1, 'B', '2016-06-20 21:00:00', 6, 7), # B2 - B3
	(28, 28, 1, 'B', '2016-06-20 21:00:00', 8, 5), # B4 - B1
	(29, 29, 1, 'C', '2016-06-21 18:00:00', 10, 11), # C2 - C3
	(30, 30, 1, 'C', '2016-06-21 18:00:00', 12, 9), # C4 - C1
	(32, 32, 1, 'D', '2016-06-21 21:00:00', 16, 13), # D4 - D1
	(33, 33, 1, 'F', '2016-06-22 18:00:00', 22, 23), # F2 - F3
	(34, 34, 1, 'F', '2016-06-22 18:00:00', 24, 21), # F4 - F1
	(35, 35, 1, 'E', '2016-06-22 21:00:00', 18, 19), # E2 - E3
	(36, 36, 1, 'E', '2016-06-22 21:00:00', 20, 17), # E4 - E1

	(37, 37, 2, null, '2016-06-25 15:00:00', null, null), # 2nd A - 2nd C
	(38, 38, 2, null, '2016-06-25 18:00:00', null, null), # Winner B - 3rd A/C/D
	(39, 39, 2, null, '2016-06-25 21:00:00', null, null), # Winner D - 3rd B/E/F
	(40, 40, 2, null, '2016-06-26 15:00:00', null, null), # Winner A - 3rd C/D/E
	(41, 41, 2, null, '2016-06-26 17:00:00', null, null), # Winner C - 3rd A/B/F
	(42, 42, 2, null, '2016-06-26 21:00:00', null, null), # Winner F - 2nd E
	(43, 43, 2, null, '2016-06-27 18:00:00', null, null), # Winner E - 2nd D
	(44, 44, 2, null, '2016-06-27 21:00:00', null, null), # Winner B - 2nd B

	(45, 45, 3, null, '2016-06-30 21:00:00', null, null), # Winner 37 - Winner 39
	(46, 46, 3, null, '2016-07-01 21:00:00', null, null), # Winner 38 - Winner 42
	(47, 47, 3, null, '2016-07-02 21:00:00', null, null), # Winner 41 - Winner 43
	(48, 48, 3, null, '2016-07-03 21:00:00', null, null), # Winner 40 - Winner 44

	(49, 49, 4, null, '2016-07-06 21:00:00', null, null), # Winner 45 - Winner 46
	(50, 50, 4, null, '2016-07-07 21:00:00', null, null), # Winner 47 - Winner 48

	(51, 51, 5, null, '2016-07-10 21:00:00', null, null); # Winner 49 - Winner 50

INSERT INTO user (id, email, firstName, lastName, password, isManager) VALUES
	# password = 'testtest' (sha-256 encoded)
	(1, 'manager@test.ch', 'John', 'Doe', '37268335dd6931045bdcdf92623ff819a64244b53d0e746d438797349d4da578', 1),
	(2, 'user@test.ch', 'Felix', 'Müller', '37268335dd6931045bdcdf92623ff819a64244b53d0e746d438797349d4da578', 0);