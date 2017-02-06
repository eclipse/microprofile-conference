/*
 * Copyright(c) 2016-2017 IBM, Red Hat, and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const webapp = 'ROOT';
const resources = 'static-resources';
const target = '../../../target';
const gulp = require('gulp');
const ts = require('gulp-typescript');
const tslint = require('gulp-tslint');
const sourcemaps = require('gulp-sourcemaps');
const uglify = require('gulp-uglify');
const cleanCSS = require('gulp-clean-css');
const concat = require('gulp-concat');
const debug = require('gulp-debug');
const del = require('del');
const gulpsync = require('gulp-sync')(gulp);
const sass = require('gulp-sass');
const es = require('event-stream');
const autoprefixer = require('gulp-autoprefixer');
const KarmaServer = require('karma').Server;
const gutil = require('gulp-util');
const tsProject = ts.createProject('tsconfig.json', {});
const babel = require('gulp-babel');
const fs = require('fs');

/**
 * Run all css & image tasks
 */
gulp.task('css', gulpsync.sync(['images', 'css-build', 'css-third-party']));

/**
 * Copy images from assets to
 */
gulp.task('images', function () {
    return gulp.src('./app/assets/**/*.{gif,jpg,png,svg,ico}')
        .pipe(gulp.dest(target + '/' + resources + '/assets'));
});

gulp.task('css-build', gulpsync.sync(['sass', 'css-concat']));

gulp.task('sass', function () {
    return gulp.src('./node_modules/bootstrap/scss/bootstrap-flex.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(autoprefixer({
            browsers: ['last 1 versions']
        }))
        .pipe(cleanCSS())
        .pipe(gulp.dest(target + '/scss/'));
});

gulp.task('css-concat', function () {
    return gulp.src(target + '/scss/*.css')
        .pipe(concat('bootstrap.min.css'))
        .pipe(gulp.dest(target + '/' + resources + '/assets/css/'))
});

gulp.task('css-third-party', function () {

    var scripts = [
        './node_modules/fullcalendar/dist/fullcalendar.min.css',
        './node_modules/font-awesome/css/font-awesome.min.css',
        './node_modules/primeng/resources/themes/afternoon/theme.css',
        './node_modules/primeng/resources/themes/afternoon/images/*.png',
        './node_modules/primeng/resources/primeng.min.css'
    ];

    var tasks = [];
    var s;
    for (s in scripts) {

        var src = scripts[s];
        var dest = target + '/' + resources + '/assets/css' + src.substr(1, src.lastIndexOf('/'));
        var file = dest + src.substr(src.lastIndexOf('/') + 1);

        try {
            fs.accessSync(file, fs.F_OK);
        } catch (e) {
            tasks.push(buildTask(src, dest));
        }
    }

    return es.concat(tasks);
});

gulp.task('js', gulpsync.sync(['compile-ts', 'js-third-party', 'js-bundles']));

gulp.task('lint-ts', function () {
    return gulp.src('./app/**/*.ts')
        .pipe(tslint())
        .pipe(tslint.report('prose'));
});

gulp.task('compile-ts', function () {

    return tsProject.src()
        .pipe(debug({title: 'input'}))
        .pipe(sourcemaps.init())
        .pipe(tsProject())
        .pipe(babel({
            presets: ['es2015']
        }))
        .pipe(uglify({
            mangle: false
        }).on('error', gutil.log))
        .pipe(sourcemaps.write({includeContent: false}))
        .pipe(gulp.dest(target + '/' + resources + '/app/'));
});

gulp.task('js-third-party', function () {

    var scripts = [
        './systemjs.config.js',
        './node_modules/core-js/client/shim.min.js',
        './node_modules/zone.js/dist/zone.js',
        './node_modules/reflect-metadata/Reflect.js',
        './node_modules/systemjs/dist/system.js',
        './node_modules/html5shiv/dist/html5shiv.min.js',
        './node_modules/respond.js/dest/respond.min.js',
        './node_modules/jquery/dist/jquery.min.js',
        './node_modules/jquery-easing/dist/jquery.easing.1.3.umd.min.js',
        './node_modules/moment/moment.js',
        './node_modules/bootstrap/dist/js/bootstrap.min.js',
        './node_modules/tether/dist/js/tether.min.js',
        './node_modules/fullcalendar/dist/fullcalendar.min.js',
        './node_modules/chart.js/dist/Chart.bundle.min.js',
        // angular2
        './node_modules/@angular/core/bundles/core.umd.js',
        './node_modules/@angular/common/bundles/common.umd.js',
        './node_modules/@angular/compiler/bundles/compiler.umd.js',
        './node_modules/@angular/platform-browser/bundles/platform-browser.umd.js',
        './node_modules/@angular/platform-browser-dynamic/bundles/platform-browser-dynamic.umd.js',
        './node_modules/@angular/http/bundles/http.umd.js',
        './node_modules/@angular/router/bundles/router.umd.js',
        './node_modules/@angular/forms/bundles/forms.umd.js',
        './node_modules/es6-promise/dist/es6-promise.min.js'
    ];

    var tasks = [];
    var s;
    for (s in scripts) {

        var src = scripts[s];
        var dest = target + '/' + resources + '/assets/js' + src.substr(1, src.lastIndexOf('/'));
        var file = dest + src.substr(src.lastIndexOf('/') + 1);

        try {
            fs.accessSync(file, fs.F_OK);
            //console.log("Synchronized: " + src + " to " + dest);
        } catch (e) {
            tasks.push(buildTask(src, dest));
        }
    }

    return es.concat(tasks);
});

function buildTask(/*String*/src, /*String*/dest) {
    console.log("Copied: " + src + " to " + dest);
    return gulp.src(src).pipe(gulp.dest(dest));
}

gulp.task('js-bundles', function () {

    var moment = gulp.src([
        './node_modules/angular2-moment/**/*.js'
    ], {base: './node_modules/angular2-moment/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/angular2-moment'));

    var primeng = gulp.src([
        './node_modules/primeng/**/*.js'
    ], {base: './node_modules/primeng/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/primeng'));

    var rxjs = gulp.src([
        './node_modules/rxjs/**/*.js'
    ], {base: './node_modules/rxjs/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/rxjs'));

    var wapi = gulp.src([
        './node_modules/angular2-in-memory-web-api/**/*.js'
    ], {base: './node_modules/angular2-in-memory-web-api/'}).pipe(gulp.dest(target + '/' + resources + '/assets/js/node_modules/angular2-in-memory-web-api'));
    return es.concat(rxjs, wapi);
});

gulp.task('pages', function () {
    var app = gulp.src([
        './app/**/*.{jsp,html,htm}'
    ], {base: '.app/'}).pipe(gulp.dest(target + '/' + resources + '/app'));

    var idx = gulp.src([
        './*.{jsp,html,htm}'
    ]).pipe(gulp.dest(target + '/' + resources + '/'));

    return es.concat(app, idx);
});

gulp.task('test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js'
    }, done).start();
});

//TODO - Create gulp tasks for all vendors

//---------TOMEE BEGIN
gulp.task('copy-tomee', function () {
    return gulp.src([
        target + '/' + resources + '/**/*.{html,htm,jsp,jspx,htm,css,js}',
        target + '/' + resources + '/assets'
    ]).pipe(gulp.dest(target + '/apache-tomee/webapps/' + webapp + '/'));
});

gulp.task('clean-tomee', function (callback) {
    return del([
        target + '/apache-tomee/webapps/' + webapp + '/app/'
    ], {
        force: true
    }, callback);
});
//---------TOMEE END

gulp.task('build', gulpsync.sync(['clean-tomee', 'js', 'css', 'pages', 'copy-tomee']));
//gulp.task('build-with-tests', gulpsync.sync(['build', 'test']));

gulp.task('default', gulpsync.sync(['build']), function () {
    gulp.watch(
        ['index.html', './app/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build'])
    );
});
