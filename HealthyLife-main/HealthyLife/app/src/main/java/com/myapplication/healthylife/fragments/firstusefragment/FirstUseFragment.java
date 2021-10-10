package com.myapplication.healthylife.fragments.firstusefragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.myapplication.healthylife.R;
import com.myapplication.healthylife.databinding.FragmentFirstUseBinding;
import com.myapplication.healthylife.local.AppPrefs;
import com.myapplication.healthylife.local.DatabaseHelper;
import com.myapplication.healthylife.model.Diet;
import com.myapplication.healthylife.model.Dish;
import com.myapplication.healthylife.model.Exercise;
import com.myapplication.healthylife.model.Stat;
import com.myapplication.healthylife.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstUseFragment extends Fragment {
    private FragmentFirstUseBinding binding;
    private NavController navController;

    private SharedPreferences sharedPreferences;
    private ArrayList<Exercise> exercises = new ArrayList<>();
    private ArrayList<Diet> diets = new ArrayList<>();
    private ArrayList<Dish> dishes = new ArrayList<>();
    private DatabaseHelper db;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateTimeSdf = new SimpleDateFormat("dd/MM/yyyy, kk:mm:ss");
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = AppPrefs.getInstance(getContext());
        db = new DatabaseHelper(getContext());

        binding = FragmentFirstUseBinding.inflate(getLayoutInflater());

        initData();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.scrollview.scrollTo(0, binding.scrollview.getBottom());
                        }
                    }, 500);
                }else   {
                    hideKeyboard(view);
                }
            }
        });

        binding.etWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.scrollview.scrollTo(0, binding.scrollview.getBottom());
                        }
                    }, 500);
                }
                else   {
                    hideKeyboard(view);
                }
            }
        });

        binding.etHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)  {
                    binding.scrollview.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            binding.scrollview.scrollTo(0, binding.scrollview.getBottom());
                        }
                    }, 500);
                }else   {
                    hideKeyboard(view);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        navController = Navigation.findNavController(getActivity(), R.id.fragmentContainer);
        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.etName.getText().toString().equals("") && validateString(binding.etName.getText().toString())) {
                    if (!binding.etHeight.getText().toString().equals("")
                            && validateFloat(binding.etHeight.getText().toString())
                            && (Float.valueOf(binding.etHeight.getText().toString()) >= 10  && Float.valueOf(binding.etHeight.getText().toString()) <= 300))    {
                        if (!binding.etWeight.getText().toString().equals("")
                                && validateFloat(binding.etWeight.getText().toString())
                                && (Float.valueOf(binding.etWeight.getText().toString()) >= 1 && Float.valueOf(binding.etWeight.getText().toString()) <= 600))    {
                            User user = new User(binding.etName.getText().toString(),
                                    Float.valueOf(binding.etHeight.getText().toString()),
                                    Float.valueOf(binding.etWeight.getText().toString()));

                            double bmi = Math.round(((user.getWeight()/Math.pow(user.getHeight()/100, 2))*10)/10);
                            Log.d("DATA", String.valueOf(bmi));
                            user.setBmi(bmi);

                            sharedPreferences.edit().putBoolean("isLogout", false).apply();

                            sharedPreferences.edit().putString("user", new Gson().toJson(user)).apply();

                            date = new Date();
                            String now = sdf.format(date);

                            sharedPreferences.edit().putString("lastLogin", now).apply();

                            saveListOfExercisesForNewUser(exercises, bmi);
                            saveListofDietForNewUser(diets, bmi);
                            saveListofDishForNewUser(dishes);
                            db.addStat(new Stat(-1, user.getHeight(), user.getWeight(), user.getBmi(), dateTimeSdf.format(date)));

                            navController.navigate(R.id.action_firstUseFragment_to_mainFragment);
                        }else   {
                            binding.etWeight.requestFocus();
                            openKeyboard(binding.etWeight);
                            Toast.makeText(getActivity(), "Invalid Weight", Toast.LENGTH_SHORT).show();
                        }
                    }else   {
                        binding.etHeight.requestFocus();
                        openKeyboard(binding.etHeight);
                        Toast.makeText(getActivity(), "Invalid Height", Toast.LENGTH_SHORT).show();
                    }
                }else   {
                    binding.etName.requestFocus();
                    openKeyboard(binding.etName);
                    Toast.makeText(getActivity(), "Invalid Name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validateString(String str)   {
        Pattern patternString = Pattern.compile("^[a-zA-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" + "ẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" + "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$");
        Matcher matcher = patternString.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private Boolean validateFloat(String num) {
        Pattern patternFloat = Pattern.compile("([0-9]*[.])?[0-9]+");
        Matcher matcher = patternFloat.matcher(num);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    private void initData() {
        exercises.add(new Exercise(-1,"Bridge", "Easy", 90, R.drawable.bridge, new int[]{1, 5}, R.raw.bridge,
                "This exercise helps strengthen your core muscles, whittles your waistline, and boosts flexibility. It is a perfect way to warm up your body",
                "- Find an open space on the floor, lie on your back floor, rest your hands at your sides, bend your knees, and place your feet flat on the floor, beneath your knees.\n- Tighten your abdominal and buttock muscles by pushing your low back into the ground.\n- Raise your hips to create a straight line from your knees to your shoulders.\n- Squeeze your core and pull your belly button back toward your spine\n- Lower the hips to return to the starting position\n- Repeat.\n\nNOTICE:\n- 15-20 reps/set.\n- 3 sets/time.\n- 10s resting between set.\n- 1 minute rest before moving to other exercise.",
                "None", 60, 10, 3, 60, 42));

        exercises.add(new Exercise(-1,"Chair Squat", "Moderate", 90, R.drawable.chairsquat, new int[]{2, 3}, R.raw.chairsquat,
                "Chair squats are a great way to build up the strength in your leg muscles. The chair provides added support as you work your glutes, hamstrings, and quads. You can make use of your chair and do this exercise almost anywhere.",
                "- Stand just in front of your chair, facing away from it. Your feet should be shoulder-width apart, with toes pointing straight ahead.\n- Keep your spine neutral and head and chest raised. Engage your core as you bend knees and lower your hips down and back. You can lift your arms out in front for extra balance while you lower yourself down.\n- Gently tap the chair with your butt, but don’t sit down.\n- Squeeze glutes and hamstrings to drive your hips forward and up, returning to the starting position.\n\nNOTICE:\n- 10-15 reps/set.\n- 3 sets/time.\n- 10s resting between set.\n- 1 minute resting before moving to other exercises.",
                "A chair", 60, 10, 2, 60, 36));

        exercises.add(new Exercise(-1,"Knee Push-up", "Moderate", 90, R.drawable.kneepushup, new int[]{3, 4}, R.raw.kneepushup,
                "Knee push-ups can improve upper-body strength, increase core stability. It is a beginner-level bodyweight exercise.",
                "- Place the knees on the floor, the hands below the shoulders, and cross your feet.\n- Keeping your back straight, start bending the elbows until your chest is almost touching the floor.\n- Pause and push back to the starting position.\n- Repeat until the set is complete.\n\nNOTICE:\n- 20-25 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None", 50, 10, 2, 60, 34));

        exercises.add(new Exercise(-1,"Stationary Lunge", "Moderate", 60, R.drawable.stationarylunge, new int[]{2, 5}, R.raw.stationarylunge,
                "Stationary lunges target your glutes, quadriceps, and hamstrings. You’ll put most of your weight on your front leg and use your back leg to balance, stabilize, and support your entire body.",
                "- Stand up straight, feet hip-width apart.\n- Put your hands on your hips for stability.\n- Tighten your abs.\n- Shift your weight forward as you take one big step in front of you, allowing your back heel to rise.\n- Sink until your forward-stepping leg is at a right angle (thigh parallel with the ground, shin vertical).\n- Press into your front heel while you push back up into the starting position.\n- Repeat on the other side.\n\nNOTICE:\n- 20-25 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",60,10,2,60,30));

        exercises.add(new Exercise(-1,"Downward Dog", "Moderate", 90, R.drawable.downwarddog, new int[]{1, 5}, R.raw.downwarddog,
                "Downward Dog helps stretch the lower body, strengthen the upper body, and stimulates blood flow. This exercise is also great for improving postures and fine-tuning your foot muscles.",
                "- On all fours, hands and knees, spread your palms wide, knees are hip distance apart, palms are spread flat.\n- Raise the body up and back into posture, your body should form a triangle with the floor, eyes looking at your legs.\n\nNOTICE:\n- 15-20 rep/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",45,10,3,60,36));

        exercises.add(new Exercise(-1,"Donkey Kick", "Moderate", 60, R.drawable.donkeykick, new int[]{2}, R.raw.donkeykick,
                "Donkey kicks are great for both stability and toning. They target your gluteus maximus—the largest of your three glutes muscles, and the bulk of your booty. They also work your core and shoulder muscles. This exercise is especially beneficial for anyone who has a desk job.",
                "- Get on all fours, with your hands stacked directly under shoulders, and knees under hips.\n- Make sure your back is flat and tuck your chin slightly so the back of your neck is facing the ceiling.\n- Without rounding your spine, engage your lower abdominals. Keeping the 90-degree bend in your right knee, slowly lift your leg straight back and up toward the ceiling.\n- Your max height is right before your back starts to arch, or your hips begin to rotate.\n- Return to the starting position. Repeat all reps on one side, then switch legs. \n\nNOTICE:\n- 15-20 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",60,10,2,60,36));

        exercises.add(new Exercise(-1,"Bird Dog", "Hard", 90, R.drawable.birddog, new int[]{3, 4}, R.raw.birddog,
                "The bird dog exercise works the erector spinae, rectus abdominis, and glutes. This allows for correct movement, control, and stability of the whole body. It’s an ideal exercise for people with low back concerns, and it can help to develop good balance and posture.\n",
                "- Begin on all fours in the tabletop position. Place your knees under your hips and your hands under your shoulders.\n- Maintain a neutral spine by engaging your abdominal muscles.\n- Draw your shoulder blades together.\n- Raise your right arm and left leg, keeping your shoulders and hips parallel to the floor.\n- Lengthen the back of your neck and tuck your chin into your chest to gaze down at the floor.\n- Hold this position for a few seconds, then lower back down to the starting position.\n- Raise your left arm and right leg, holding this position for a few seconds.\n- Return to the starting position. This is one round.\n\nNOTICE:\n- 15-20 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",45,10,2,60,24));

        exercises.add(new Exercise(-1,"Forearm Plank", "Hard", 60, R.drawable.forearmplank, new int[]{4}, R.raw.forearmplank,
                "The Forearm Plank strengthens the abs, legs and core. It is also good for stretching the arches of your feet as well as your calves, shoulders and hamstrings to support the spine in day-to-day activities.",
                "- Start lying face down on the floor with your forearms flat on the ground. Make sure that your elbows are directly under the shoulders.\n- Fire up the core and raise your body up off the ground.\n- Engage your core and keep your body in a straight line- avoid letting your hips rise or drop.\n- Breathe and hold.\n\nNOTICE:\n- 15-30 rep/set.\n- 1 set/time.\n- 1 minute resting before moving to other exercises.",
                "None",60,0,1,60,20));

        exercises.add(new Exercise(-1,"Hip Abduction", "Moderate", 90, R.drawable.hipabduction, new int[]{2, 1}, R.raw.hipabduction,
                "Not only can hip abduction exercises help you get a tight and toned backside, they can also help to prevent and treat pain in the hips and knees.",
                "- \n- Lie down on the floor on your side. Rest your head on your arm. Bend your legs at the knees.\n- Keep your feet together and lift your top leg up so that your knees are separated. Keep your hips steady.\n- Slowly lower your leg back down.\n-Switch sides.\n\nNOTICE:\n- 15-20 rep/set.\n- 2 set/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",45,10,2,60,30));

        exercises.add(new Exercise(-1,"Bicycle Crunch", "Hard", 70, R.drawable.bicyclecrunch, new int[]{1, 3}, R.raw.bicyclecrunch,
                "The bicycle crunch is excellent for activating the rectus abdominis, your upper abdominal muscle, and the obliques—your side abdominal muscles. You also engage the transverse abdominis, which is the deep ab muscle that is hard to target. Besides working your abs, you will also be toning your thighs as both your hamstrings and quads will be involved with bicycling.",
                "- Lie down on your back. Bend your knees and plant your feet on the floor, hip-width apart. Place your arms behind your head, pointing your elbows outward.\n- Brace your abs. Lift your knees to 90 degrees and raise your upper body. This is your starting position.\n- Exhale and rotate your trunk, moving your right elbow and left knee toward each other. Simultaneously straighten your right leg. Pause.\n- Inhale and return to starting position.\n- Exhale. Move your left elbow to your right knee and extend your left leg. Pause. This completes 1 rep.\n\nNOTICE:\n- 10-15 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",60,10,2,60,48));

        exercises.add(new Exercise(-1,"Running", "Moderate", 300, R.drawable.running, new int[]{3, 4, 5}, R.raw.running,
                "Running not only helps your heart and lungs but also gives you a fit, healthy body and an optimistic mind.",
                "- Warm up your body\n- Run for 15 minutes.\n- Keep your back straight, face looking forward, hands swinging naturally, and breathe in and exhale deeply\n\nNOTICE:\n- Keep your pace suitable to your health condition",
                "Shoe (Optional)",900,0,1,60,200));

        exercises.add(new Exercise(-1,"Skipping Rope", "Moderate", 70, R.drawable.skippingrope, new int[]{4, 5}, R.raw.skippingrope,
                "The benefits of jumping rope include burning calories, better coordination, stronger bones, a lower injury risk, and improved heart health.\n.",
                "- Stand straight with your feet close to each other.\n- Swing the ropes and jump\n\nNOTICE:\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "Ropes",60,10,3,60,120));

        exercises.add(new Exercise(-1,"Push Up", "Hard", 90, R.drawable.pushup, new int[]{1, 2}, R.raw.pushup,
                "Traditional push-ups are beneficial for building upper body strength. They work the triceps, pectoral muscles, and shoulders. When done with proper form, they can also strengthen the lower back and core.",
                "- Get down on all fours, placing your hands slightly wider than your shoulders.\n-Straighten your arms and legs.\n- Lower your body until your chest nearly touches the floor.\n-Pause, then push yourself back up.\n-Repeat.\n\nNOTICE:\n- 10-20 reps/set.\n- 2 sets/time.\n- 10s resting between sets.\n- 1 minute resting before moving to other exercises.",
                "None",45,10,3,60,70));

        diets.add(new Diet(-1, "Low-carb Diet","Diets with restriction on carbohydrate-rich products. The primary aim of the diet is to force your body to use more fats for fuel instead of using carbs as a main source of energy.","In extremely rare cases, low-carb diets can cause a serious condition called nondiabetic ketoacidosis. This condition seems to be more common in lactating women and can be fatal if left untreated.",1800,new int[]{2, 3, 4, 5},false,false, true, false, R.drawable.eggnveg));
        diets.add(new Diet(-1,"Vegan Diet","A vegan diet excludes all animal products.","Vegan diets is effective at helping people naturally reduce the amount of calories they eat, resulting in weight loss.However,Vegans may be at an increased risk of certain nutrient deficiencies.",1500, new int[]{2,3,4},false,true,true, true, R.drawable.peasntofu));
        diets.add(new Diet(-1,"3k Diet", "A diet to gain weight for underweight people","The menu shown later is only cover 75% amount of calories needed. Keep exercising for balance, or you will be overwhelmed by the calories taken in.", 3000, new int[]{1}, false,true,true, false, R.drawable.porknveg));

        dishes.add(new Dish(-1, "Bacon and egg"," No Description",
                "Fried Bacon and egg in medium heat in 3 mins, until it is done",
                "Eat with green veggies for fiber + vitamins","2-3 eggs + the amount of bacon you want",
                R.drawable.eggnbacon,true, false, false, true,false, false));
        dishes.add(new Dish(-1, "Avocado salad"," No Description",
                "Prepare and wash lettuce, avocado, bell chillies, cucumber. Mix them all together with  Vinaigrette",
                "Non-vegan can top up with eggs  for diversity","Lettuce,cucumber, avocados, bell chillies, Vinaigrette  ",
                R.drawable.avocadosalad,false, false, true, false,false, true));
        dishes.add(new Dish(-1, "Nuts milk with chia seed"," No Description",
                "Use any types of nuts milk such as almond milk, soy milk, walnuts milk... and drop 1 tablespoon of chia seed in",
                "Eat with fruits(apples, strawberries) if you like","Chia Seed, nuts milk"  ,
                R.drawable.milknchia,true, false, true, false,true, true));
        dishes.add(new Dish(-1, "Green pea with tofu/beef  ","No Description ",
                "Wash green pea, season tofu/beef with garlics, soy bean. After stir frying tofu/beef for 2-3 min with medium/high flame, add the green peas, continue stirring for 5 min and turr off the heat.",
                "Non-vegan can cook and eat with beef, Vegan can do the same with tofu, people with no restriction in carb can eat with rice",
                "Green peas, tofu/beef, soy sauce, garlic, ",
                R.drawable.peasntofu,true, false, true, false,false,true));
        dishes.add(new Dish(-1, "Omelet with veggies","No Description ",
                "Break eggs, fry with olive oil for few mins. Meanwhile prepare boiled veggies (carrot,raddish, brocoli) ",
                "No note","Eggs, vegetables ( carrot/brocoli/etc...) ",
                R.drawable.eggnveg,true, false, false, true,false, false));
        dishes.add(new Dish(-1, "(Nuts) Yogurt with strawberries and nuts","No Description ",
                "Just mix the yogurt with berries and nuts (any type you want)",
                "Non-vegan can have cow milk yogurt, Vegan can alternate with nuts yogurt","Yogurt(nuts or normal), strawberries/blueberries, nuts(almond, walnuts...) ",
                R.drawable.yogurtstrawberries,true, false,true, true,true, false));
        dishes.add(new Dish(-1, "Pork chop with veggies  ","No Description ",
                "wash pork and boil it with salt (1 coffee spoon) until bubbles come up, after that countinue doing so for 15 min(or until the red part inside is gone).Meanwhile wash any kind of green veggies (cabbage, amaranth,brocoli...) and boil it after finishing the meat.",
                "No note","Pork, favorite veggies",
                R.drawable.porknveg,true, false, false, false,true, true));
    }
    private void saveListofDietForNewUser(ArrayList<Diet> diets, double bmi){
        boolean startRecommended = false;
        boolean startOthers = false;
        ArrayList<Diet> result = new ArrayList<>();
        int type;
        if (bmi >= 35) {
            type = 5;
        }else if(bmi >= 30 && bmi <= 34.9) {
            type = 4;
        }else if(bmi >= 25 && bmi <= 29.9)  {
            type = 3;
        }else if(bmi >= 18.5 && bmi <= 24.9)    {
            type = 2;
        }else   {
            type = 1;
        }
        Log.d("DATA", String.valueOf(type));
        for (Diet d: diets)    {
            for (int i: d.getTypes())  {
                if (i == type && !startRecommended)  {
                    d.setRecommended(true);
                    Log.d("REC", d.getName()+" "+d.isRecommended());
                    result.add(d);
                    startRecommended = true;
                    break;
                }else if(i == type && startRecommended) {
                    d.setRecommended(true);
                    Log.d("REC", d.getName()+" "+d.isRecommended());
                    result.add(d);
                    break;
                }
            }
        }
        for (Diet d:result){
            db.addDiet(d);
        }
    }
    private void saveListofDishForNewUser(ArrayList<Dish> Dishes){

        for (Dish d:Dishes){
            db.addDish(d);
        }
    }
    private void saveListOfExercisesForNewUser(ArrayList<Exercise> exercise, double bmi)    {
        boolean startRecommended = false;
        boolean startOthers = false;
        ArrayList<Exercise> result = new ArrayList<>();
        int type;
        if (bmi >= 35) {
            type = 5;
        }else if(bmi >= 30 && bmi <= 34.9) {
            type = 4;
        }else if(bmi >= 25 && bmi <= 29.9)  {
            type = 3;
        }else if(bmi >= 18.5 && bmi <= 24.9)    {
            type = 2;
        }else   {
            type = 1;
        }
        Log.d("DATA", String.valueOf(type));

        //add recommended ex
        for (Exercise ex: exercise)    {
            for (int i: ex.getTypes())  {
                if (i == type && !startRecommended)  {
                    ex.setFirst(true);
                    ex.setRecommended(true);
                    Log.d("REC", ex.getName()+" "+ex.isRecommended()+" "+ex.isOthers()+" "+ex.isFirst());
                    result.add(ex);
                    startRecommended = true;
                    break;
                }else if(i == type && startRecommended) {
                    ex.setRecommended(true);
                    Log.d("REC", ex.getName()+" "+ex.isRecommended()+" "+ex.isOthers()+" "+ex.isFirst());
                    result.add(ex);
                    break;
                }
            }
        }

        boolean isOthers;
        for (Exercise ex: exercise)    {
            isOthers = true;
            for (int i: ex.getTypes()) {
                if(i == type)    {
                    isOthers = false;
                }
            }
            if (isOthers && !startOthers) {
                ex.setFirst(true);
                ex.setOthers(true);
                Log.d("OTH", ex.getName()+" "+ex.isRecommended()+" "+ex.isOthers()+" "+ex.isFirst());
                result.add(ex);
                startOthers = true;
            } else if (isOthers && startOthers) {
                ex.setOthers(true);
                Log.d("OTH", ex.getName()+" "+ex.isRecommended()+" "+ex.isOthers()+" "+ex.isFirst());
                result.add(ex);
            }
        }

        for (Exercise ex: result) {
            Log.d("DATA", ex.getName()+" "+ex.isRecommended()+" "+ex.isOthers()+" "+ex.isFirst());
            db.addExercise(ex);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public void openKeyboard(View view)  {
        InputMethodManager inputMethodManager =  (InputMethodManager)getContext().getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(),     InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideKeyboard(View view)  {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}