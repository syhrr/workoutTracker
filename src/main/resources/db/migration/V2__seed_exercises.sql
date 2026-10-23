INSERT INTO exercise (name, description, exercise_category, target_muscle) VALUES
-- Chest
('Barbell Bench Press',      'Press a barbell from the chest while lying on a flat bench',          'CHEST', 'CHEST'),
('Incline Barbell Press',    'Bench press on a 30-45 degree incline to target the upper chest',     'CHEST', 'CHEST'),
('Dumbbell Bench Press',     'Press dumbbells from the chest on a flat bench',                      'CHEST', 'CHEST'),
('Incline Dumbbell Press',   'Dumbbell press on an incline bench',                                  'CHEST', 'CHEST'),
('Dumbbell Fly',             'Arc dumbbells out and together with a slight elbow bend',             'CHEST', 'CHEST'),
('Cable Crossover',          'Bring cable handles together in front of the chest',                  'CHEST', 'CHEST'),
('Chest Dip',                'Lower and press up on parallel bars leaning forward',                 'CHEST', 'CHEST'),
('Push-Up',                  'Press the body up from the floor with hands under the shoulders',     'CHEST', 'CHEST'),

-- Back
('Pull-Up',                  'Pull the chin over a bar from a dead hang, overhand grip',            'BACK', 'LATS'),
('Chin-Up',                  'Pull the chin over a bar from a dead hang, underhand grip',           'BACK', 'LATS'),
('Lat Pulldown',             'Pull a cable bar down to the upper chest',                            'BACK', 'LATS'),
('Barbell Row',              'Row a barbell to the lower chest from a hip-hinged position',         'BACK', 'UPPER_BACK'),
('Dumbbell Row',             'Single-arm row with one hand and knee braced on a bench',             'BACK', 'LATS'),
('Seated Cable Row',         'Row a cable handle to the torso while seated',                        'BACK', 'UPPER_BACK'),
('T-Bar Row',                'Row a loaded T-bar to the chest from a hinged position',              'BACK', 'UPPER_BACK'),
('Face Pull',                'Pull a rope attachment toward the face to target the rear delts',     'BACK', 'REAR_DELTS'),

-- Legs
('Back Squat',               'Squat with a barbell across the upper back',                          'LEGS', 'QUADS'),
('Front Squat',              'Squat with a barbell racked across the front of the shoulders',       'LEGS', 'QUADS'),
('Romanian Deadlift',        'Hip hinge with a barbell and a slight knee bend',                     'LEGS', 'HAMSTRINGS'),
('Leg Press',                'Press a weighted sled away on a leg press machine',                   'LEGS', 'QUADS'),
('Walking Lunge',            'Step forward into a lunge, alternating legs',                         'LEGS', 'QUADS'),
('Bulgarian Split Squat',    'Single-leg squat with the rear foot elevated on a bench',             'LEGS', 'QUADS'),
('Leg Extension',            'Extend the knees against resistance on a machine',                    'LEGS', 'QUADS'),
('Lying Leg Curl',           'Curl the heels toward the glutes on a machine',                       'LEGS', 'HAMSTRINGS'),
('Hip Thrust',               'Drive the hips up with the upper back against a bench',               'LEGS', 'GLUTES'),
('Standing Calf Raise',      'Rise onto the toes under load',                                       'LEGS', 'CALVES'),

-- Shoulders
('Overhead Press',           'Press a barbell from the shoulders to overhead while standing',       'SHOULDERS', 'FRONT_DELTS'),
('Seated Dumbbell Press',    'Press dumbbells overhead from a seated position',                     'SHOULDERS', 'FRONT_DELTS'),
('Arnold Press',             'Dumbbell press with a rotation from palms-in to palms-forward',       'SHOULDERS', 'FRONT_DELTS'),
('Lateral Raise',            'Raise dumbbells out to the sides to shoulder height',                 'SHOULDERS', 'SIDE_DELTS'),

-- Arms
('Recline Curl',             'Curl a dumbbell with elbow support',                                  'ARMS', 'BICEPS'),

-- Core
('Lat Pulldown Ab Crunch',   'Ab crunch on a lat pulldown',                                         'CORE', 'ABS');
